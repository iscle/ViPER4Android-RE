package com.aam.viper4android.util

import android.os.Build
import android.util.Log
import android.util.Property
import java.lang.reflect.Method

object HiddenApi {
    private const val TAG = "HiddenApi"
    private var unsealed = false

    // Inspired by https://github.com/michalbednarski/LeakValue/blob/b0a2e05c079d2cf8a1e6af208870db1885ac9064/app/src/main/java/com/example/leakvalue/MiscUtils.java#L228
    fun unseal() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) return
        if (unsealed) return
        unsealed = true

        try {
            val methods = Property.of(Class::class.java, Array<Method>::class.java, "Methods")
                .get(Class.forName("dalvik.system.VMRuntime"))
            val setHiddenApiExemptions = methods.first { it.name == "setHiddenApiExemptions" }
            val getRuntime = methods.first { it.name == "getRuntime" }
            setHiddenApiExemptions.invoke(getRuntime.invoke(null), arrayOf("L"))
            Log.d(TAG, "unseal: success")
        } catch (e: Exception) {
            Log.e(TAG, "unseal: failed", e)
        }
    }
}