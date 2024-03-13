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
            intent.action != AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION) return

        val packageName = intent.getStringExtra(AudioEffect.EXTRA_PACKAGE_NAME)
        val audioSession = intent.getIntExtra(AudioEffect.EXTRA_AUDIO_SESSION, -1)
        if (packageName == null || audioSession == -1) {
            Log.e(TAG, "onReceive: Missing required extras")
            return
        }

        Intent(context, ViPERService::class.java)
            .setAction(intent.action)
            .putExtra(AudioEffect.EXTRA_PACKAGE_NAME, packageName)
            .putExtra(AudioEffect.EXTRA_AUDIO_SESSION, audioSession)
            .let {
                try {
                    ContextCompat.startForegroundService(context, it)
                } catch (e: Exception) {
                    Log.e(TAG, "onReceive: Failed to start service", e)
                    FirebaseCrashlytics.getInstance().recordException(e)
                }
            }
    }
}