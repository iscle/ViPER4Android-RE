package com.aam.viper4android.ktx

import android.media.audiofx.AudioEffect
import java.lang.reflect.InvocationTargetException
import java.util.*

private val constructor = AudioEffect::class.java.getDeclaredConstructor(
    UUID::class.java,
    UUID::class.java,
    Integer.TYPE,
    Integer.TYPE
)

private val setParameterMethod = AudioEffect::class.java.getDeclaredMethod(
    "setParameter",
    ByteArray::class.java,
    ByteArray::class.java
)

private val getParameterMethod = AudioEffect::class.java.getDeclaredMethod(
    "getParameter",
    ByteArray::class.java,
    ByteArray::class.java
)

fun AudioEffectKtx(type: UUID, uuid: UUID, priority: Int, audioSession: Int) = try {
    constructor.newInstance(type, uuid, priority, audioSession) as AudioEffect
} catch (e: InvocationTargetException) {
    throw e.targetException
}

fun AudioEffect.setParameterKtx(param: ByteArray, value: ByteArray) = try {
    setParameterMethod.invoke(this, param, value) as Int
} catch (e: InvocationTargetException) {
    throw e.targetException
}

fun AudioEffect.getParameterKtx(param: ByteArray, value: ByteArray) = try {
    getParameterMethod.invoke(this, param, value) as Int
} catch (e: InvocationTargetException) {
    throw e.targetException
}