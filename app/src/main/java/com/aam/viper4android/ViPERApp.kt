package com.aam.viper4android

import android.app.Application

class ViPERApp : Application() {
    companion object {
        init {
            System.loadLibrary("hiddenapibypass")
        }
    }
}