package com.aam.viper4android.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.audiofx.AudioEffect
import android.util.Log
import androidx.core.content.ContextCompat
import com.aam.viper4android.ViPERService
import com.google.firebase.crashlytics.FirebaseCrashlytics

private const val TAG = "SessionReceiver"

class SessionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        if (intent.action != AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION &&
            intent.action != AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION) {
            Log.d(TAG, "onReceive: Invalid action: ${intent.action}")
            return
        }

        val packageName = intent.getStringExtra(AudioEffect.EXTRA_PACKAGE_NAME)
        val audioSession = intent.getIntExtra(AudioEffect.EXTRA_AUDIO_SESSION, -1)
        val contentType = intent.getIntExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC)

        Intent(context, ViPERService::class.java).let {
            it.action = intent.action
            it.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, packageName)
            it.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, audioSession)
            it.putExtra(AudioEffect.EXTRA_CONTENT_TYPE, contentType)
            try {
                ContextCompat.startForegroundService(context, it)
            } catch (e: Exception) {
                Log.e(TAG, "onReceive: Failed to start service", e)
                FirebaseCrashlytics.getInstance().recordException(e)
            }
        }
    }
}