package com.aam.viper4android

enum class DisableReason(val value: Int) {
    UNKNOWN(-1),
    NONE(0),
    INVALID_FRAME_COUNT(1),
    INVALID_SAMPLING_RATE(2),
    INVALID_CHANNEL_COUNT(3),
    INVALID_FORMAT(4);

    companion object {
        fun fromValue(value: Int) = entries.firstOrNull { it.value == value } ?: UNKNOWN
    }
}