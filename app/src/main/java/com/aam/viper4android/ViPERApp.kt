package com.aam.viper4android

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.aam.viper4android.util.HiddenApi
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ViPERApp : Application() {
    companion object {
        const val SERVICES_CHANNEL_ID = "services_channel"
    }

    override fun onCreate() {
        super.onCreate()
        HiddenApi.unseal()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                SERVICES_CHANNEL_ID,
                getString(R.string.viper_notification_channel_services_name),
                NotificationManager.IMPORTANCE_LOW
            ).also {
                it.description = getString(R.string.viper_notification_channel_services_description)
            }
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }
}