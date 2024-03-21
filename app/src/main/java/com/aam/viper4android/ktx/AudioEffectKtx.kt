package com.aam.viper4android.ktx

import android.media.audiofx.AudioEffect
import java.lang.reflect.InvocationTargetException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.UUID

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

private fun getKeyAsParam(key: UInt) =
    ByteBuffer.allocate(4).order(ByteOrder.nativeOrder()).putInt(key.toInt()).array()

private fun checkParameterLength(length: Int, expected: Int, unknownLength: Boolean = false) {
    if (length != expected) {
        when {
            length == AudioEffect.ERROR_BAD_VALUE -> throw RuntimeException("Failed to get parameter (bad value)")
            length == AudioEffect.ERROR_INVALID_OPERATION -> throw RuntimeException("Failed to get parameter (invalid operation)")
            length == AudioEffect.ERROR_NO_MEMORY -> throw RuntimeException("Failed to get parameter (no memory)")
            length == AudioEffect.ERROR_DEAD_OBJECT -> throw RuntimeException("Failed to get parameter (dead object)")
            unknownLength -> return
            else -> throw RuntimeException("Failed to get parameter (expected $expected, got $length)")
        }
    }
}

private fun checkSetParameterResponse(response: Int) {
    when (response) {
        AudioEffect.SUCCESS -> return
        AudioEffect.ERROR_BAD_VALUE -> throw RuntimeException("Failed to set parameter (bad value)")
        AudioEffect.ERROR_INVALID_OPERATION -> throw RuntimeException("Failed to set parameter (invalid operation)")
        AudioEffect.ERROR_NO_MEMORY -> throw RuntimeException("Failed to set parameter (no memory)")
        AudioEffect.ERROR_DEAD_OBJECT -> throw RuntimeException("Failed to set parameter (dead object)")
        else -> throw RuntimeException("Failed to set parameter (unknown error: $response)")
    }
}

fun AudioEffect.getBooleanParameter(key: UInt): Boolean {
    val param = getKeyAsParam(key)
    val value = ByteArray(1)
    val length = getParameterKtx(param, value)
    checkParameterLength(length, value.size)
    return value[0] != 0.toByte()
}

fun AudioEffect.getLongParameter(key: UInt): Long {
    val param = getKeyAsParam(key)
    val value = ByteArray(8)
    val length = getParameterKtx(param, value)
    checkParameterLength(length, value.size)
    return ByteBuffer.wrap(value).order(ByteOrder.nativeOrder()).long
}

fun AudioEffect.getULongParameter(key: UInt): ULong {
    return getLongParameter(key).toULong()
}

fun AudioEffect.getIntParameter(key: UInt): Int {
    val param = getKeyAsParam(key)
    val value = ByteArray(4)
    val length = getParameterKtx(param, value)
    checkParameterLength(length, value.size)
    return ByteBuffer.wrap(value).order(ByteOrder.nativeOrder()).int
}

fun AudioEffect.getUIntParameter(key: UInt): UInt {
    return getIntParameter(key).toUInt()
}

fun AudioEffect.getByteArrayParameter(key: UInt, size: Int): ByteArray {
    val param = getKeyAsParam(key)
    val value = ByteArray(size)
    val length = getParameterKtx(param, value)
    checkParameterLength(length, value.size)
    return value
}

fun AudioEffect.getByteParameter(key: UInt): Byte {
    val param = getKeyAsParam(key)
    val value = ByteArray(1)
    val length = getParameterKtx(param, value)
    checkParameterLength(length, value.size)
    return value[0]
}

fun AudioEffect.getUByteParameter(key: UInt): UByte {
    return getByteParameter(key).toUByte()
}

fun AudioEffect.setBooleanParameter(key: UInt, value: Boolean) {
    val param = getKeyAsParam(key)
    val data = byteArrayOf(if (value) 1 else 0)
    val response = setParameterKtx(param, data)
    checkSetParameterResponse(response)
}

fun AudioEffect.setLongParameter(key: UInt, value: Long) {
    val param = getKeyAsParam(key)
    val data = ByteBuffer.allocate(8).order(ByteOrder.nativeOrder()).putLong(value).array()
    val response = setParameterKtx(param, data)
    checkSetParameterResponse(response)
}

fun AudioEffect.setULongParameter(key: UInt, value: ULong) {
    setLongParameter(key, value.toLong())
}

fun AudioEffect.setIntParameter(key: UInt, value: Int) {
    val param = getKeyAsParam(key)
    val data = ByteBuffer.allocate(4).order(ByteOrder.nativeOrder()).putInt(value).array()
    val response = setParameterKtx(param, data)
    checkSetParameterResponse(response)
}

fun AudioEffect.setUIntParameter(key: UInt, value: UInt) {
    setIntParameter(key, value.toInt())
}

fun AudioEffect.setByteArrayParameter(key: UInt, value: ByteArray) {
    val param = getKeyAsParam(key)
    val response = setParameterKtx(param, value)
    checkSetParameterResponse(response)
}

fun AudioEffect.setByteParameter(key: UInt, value: Byte) {
    val param = getKeyAsParam(key)
    val data = byteArrayOf(value)
    val response = setParameterKtx(param, data)
    checkSetParameterResponse(response)
}

@OptIn(ExperimentalUnsignedTypes::class)
fun AudioEffect.setUByteArrayParameter(key: UInt, value: UByteArray) {
    setByteArrayParameter(key, value.toByteArray())
}

fun AudioEffect.setUByteParameter(key: UInt, value: UByte) {
    setByteParameter(key, value.toByte())
}

fun AudioEffect.setShortParameter(key: UInt, value: Short) {
    val param = getKeyAsParam(key)
    val data = ByteBuffer.allocate(2).order(ByteOrder.nativeOrder()).putShort(value).array()
    val response = setParameterKtx(param, data)
    checkSetParameterResponse(response)
}

fun AudioEffect.setUShortParameter(key: UInt, value: UShort) {
    setShortParameter(key, value.toShort())
}