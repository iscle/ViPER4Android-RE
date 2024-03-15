package com.aam.viper4android.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

private const val TAG = "BootCompletedReceiver"

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        // FIXME: Disabled for now. Only useful if user wants to have the service
        //  always running to avoid background restrictions
//        Intent(context, ViPERService::class.java).let {
//            try {
//                ContextCompat.startForegroundService(context, it)
//            } catch (e: Exception) {
//                Log.e(TAG, "onReceive: Failed to start service", e)
//                FirebaseCrashlytics.getInstance().recordException(e)
//            }
//        }
    }
}