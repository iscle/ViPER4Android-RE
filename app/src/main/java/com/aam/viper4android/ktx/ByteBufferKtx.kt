package com.aam.viper4android.ktx

import java.nio.ByteBuffer

fun ByteBuffer.putUByte(value: UByte): ByteBuffer = put(value.toByte())