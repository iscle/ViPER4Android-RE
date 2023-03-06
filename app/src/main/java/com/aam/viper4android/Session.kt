package com.aam.viper4android

class Session(val packageName: String, val sessionId: Int, val contentType: Int) {
    val effect = ViPEREffect(sessionId)

    fun release() {
        effect.release()
    }
}