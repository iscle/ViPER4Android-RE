#include <jni.h>
#include <future>

static jobject
getDeclaredMethodInternal(JavaVM *vm, jobject clazz, jstring name, jobjectArray parameterTypes) {
    JNIEnv *env;
    jint ret;

    ret = vm->AttachCurrentThread(&env, nullptr);
    if (ret != JNI_OK) {
        // Failed to attach current thread
        return nullptr;
    }

    jclass clazzClass = env->GetObjectClass(clazz);
    jmethodID getDeclaredMethodMethodId = env->GetMethodID(
            clazzClass, "getDeclaredMethod",
            "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;");

    jobject result = env->CallObjectMethod(
            clazz, getDeclaredMethodMethodId, name, parameterTypes);
    if (env->ExceptionCheck()) {
        env->ExceptionDescribe();
        env->ExceptionClear();
        return nullptr;
    }

    jobject gResult = nullptr;
    if (result != nullptr) {
        gResult = env->NewGlobalRef(result);
    }

    vm->DetachCurrentThread();

    return gResult;
}

static jobject getDeclaredMethod(
        JavaVM *vm, JNIEnv *env, jobject clazz, jstring name, jobjectArray parameterTypes) {
    jobject gClazz = env->NewGlobalRef(clazz);
    jstring gName = (jstring) env->NewGlobalRef(name);
    jobjectArray gParameterTypes = nullptr;
    if (parameterTypes != nullptr) {
        gParameterTypes = (jobjectArray) env->NewGlobalRef(parameterTypes);
    }

    auto future = std::async(
            getDeclaredMethodInternal, vm, gClazz, gName, gParameterTypes);
    auto gResult = future.get();

    // Delete global refs after async method has returned
    env->DeleteGlobalRef(gClazz);
    env->DeleteGlobalRef(gName);
    if (parameterTypes != nullptr) {
        env->DeleteGlobalRef(gParameterTypes);
    }

    // Check if any exception has occurred
    if (env->ExceptionCheck() == JNI_TRUE) {
        env->ExceptionDescribe();
        env->ExceptionClear();
        if (gResult != nullptr) env->DeleteGlobalRef(gResult);
        return nullptr;
    }

    if (gResult != nullptr) {
        jmethodID setAccessibleMethodId = env->GetMethodID(
                env->GetObjectClass(gResult), "setAccessible", "(Z)V");
        env->CallVoidMethod(gResult, setAccessibleMethodId, JNI_TRUE);
    }

    return gResult;
}

static void unseal(JavaVM *vm) {
    if (android_get_device_api_level() < __ANDROID_API_P__) return;

    JNIEnv *env;
    jint ret;

    ret = vm->GetEnv((void **) &env, JNI_VERSION_1_6);
    if (ret != JNI_OK) {
        // Failed to get JNI environment
        return;
    }

    jclass vmRuntimeClass = env->FindClass("dalvik/system/VMRuntime");
    jobject getRuntimeMethod = getDeclaredMethod(
            vm, env, vmRuntimeClass, env->NewStringUTF("getRuntime"), nullptr);
    if (getRuntimeMethod == nullptr) return;
    jobject runtime = env->CallStaticObjectMethod(
            vmRuntimeClass, env->FromReflectedMethod(getRuntimeMethod));

    // Delete getRuntimeMethod global ref after obtaining the runtime
    env->DeleteGlobalRef(getRuntimeMethod);

    jclass stringArrayClass = env->FindClass("[Ljava/lang/String;");
    jobjectArray classArray = env->NewObjectArray(
            1, env->FindClass("java/lang/Class"), stringArrayClass);
    jobject setHiddenApiExemptionsMethod = getDeclaredMethod(
            vm, env, vmRuntimeClass, env->NewStringUTF("setHiddenApiExemptions"), classArray);
    if (setHiddenApiExemptionsMethod == nullptr) return;

    // Exempt all classes from hidden API checks
    jobjectArray exemptions = env->NewObjectArray(
            1, env->FindClass("java/lang/String"), env->NewStringUTF("L"));
    env->CallVoidMethod(
            runtime, env->FromReflectedMethod(setHiddenApiExemptionsMethod), exemptions);

    // Delete setHiddenApiExemptionsMethod global ref after setting exemptions
    env->DeleteGlobalRef(setHiddenApiExemptionsMethod);
}

extern "C" JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    unseal(vm);
    return JNI_VERSION_1_6;
}