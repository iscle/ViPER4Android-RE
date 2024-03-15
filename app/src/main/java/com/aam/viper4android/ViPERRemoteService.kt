package com.aam.viper4android

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViPERRemoteService : Service() {
    lateinit var viperManager: ViPERManager
    private var messenger: Messenger? = null

    private class IncomingHandler(private val viperManager: ViPERManager) : Handler(Looper.getMainLooper()) {
        private val TAG = "ViPERRemoteService"
        private val MSG_GET_PARAMETER = 1
        private val MSG_GET_PARAMETER_EFFECT_ANALOGX = 1
        private val MSG_GET_PARAMETER_EFFECT_ANALOGX_ENABLED = 1

        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_GET_PARAMETER -> handleGetParameter(msg, msg.arg1, msg.arg2)
                else -> super.handleMessage(msg)
            }
        }

        private fun handleGetParameter(msg: Message, effect: Int, parameter: Int) {
            when (effect) {
                MSG_GET_PARAMETER_EFFECT_ANALOGX -> {
                    when (parameter) {
                        MSG_GET_PARAMETER_EFFECT_ANALOGX_ENABLED -> {

                        }
                        else -> {
                            Log.e(TAG, "handleGetParameter: Unknown AnalogX parameter: $parameter")
                        }
                    }
                }
                else -> {
                    Log.e(TAG, "handleGetParameter: Unknown effect: $effect")
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return messenger.let {
            it ?: Messenger(IncomingHandler(viperManager)).also { messenger = it }
        }.binder
    }
}