package com.aam.viper4android

class ActiveSession(val packageName: String, val sessionId: Int) {
    val effect = ViPEREffect(sessionId)

    fun release() {
        effect.release()
    }
}