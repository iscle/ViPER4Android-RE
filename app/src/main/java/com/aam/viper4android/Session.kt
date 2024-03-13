package com.aam.viper4android

data class Session(val packageName: String, val sessionId: Int) {
    val effect = ViPEREffect(sessionId)
}