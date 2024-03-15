package com.aam.viper4android

import java.nio.ByteBuffer
import java.nio.ByteOrder

data class Configs(
    val input: Config,
    val output: Config,
) {
    data class Config(
        val frameCount: ULong,
        val samplingRate: UInt,
        val channels: UInt,
        val format: UByte,
        val accessMode: UByte,
        val mask: UShort,
    )

    companion object {
        fun fromBytes(bytes: ByteArray): Configs {
            val bbuf = ByteBuffer.wrap(bytes).order(ByteOrder.nativeOrder())
            val input = Config(
                frameCount = bbuf.long.toULong(),
                samplingRate = bbuf.int.toUInt(),
                channels = bbuf.int.toUInt(),
                format = bbuf.get().toUByte(),
                accessMode = bbuf.get().toUByte(),
                mask = bbuf.getShort().toUShort(),
            )
            val output = Config(
                frameCount = bbuf.long.toULong(),
                samplingRate = bbuf.int.toUInt(),
                channels = bbuf.int.toUInt(),
                format = bbuf.get().toUByte(),
                accessMode = bbuf.get().toUByte(),
                mask = bbuf.getShort().toUShort(),
            )
            return Configs(input, output)
        }
    }
}
