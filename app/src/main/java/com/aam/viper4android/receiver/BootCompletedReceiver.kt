package com.aam.viper4android.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.aam.viper4android.ViPERService
import com.google.firebase.crashlytics.FirebaseCrashlytics

private const val TAG = "BootCompletedReceiver"

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        Intent(context, ViPERService::class.java).let {
            try {
                ContextCompat.startForegroundService(context, it)
            } catch (e: Exception) {
                Log.e(TAG, "onReceive: Failed to start service", e)
                FirebaseCrashlytics.getInstance().recordException(e)
            }
        }
    }
}