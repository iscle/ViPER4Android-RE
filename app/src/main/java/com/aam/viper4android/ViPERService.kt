package com.aam.viper4android

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.media.audiofx.AudioEffect
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.mediarouter.media.MediaRouter
import com.aam.viper4android.ktx.getDisplayName
import com.aam.viper4android.persistence.ViPERSettings
import com.aam.viper4android.util.AndroidUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ViPERService : Service() {
    private val TAG = "ViPERService"
    @Inject lateinit var viperManager: ViPERManager
    @Inject lateinit var viperSettings: ViPERSettings

    private val managerListener = object : ViPERManager.Listener() {
        override fun onSelectedMediaRouteChanged(viperManager: ViPERManager, route: MediaRouter.RouteInfo) {
            updateNotification(route = route)
        }

        override fun onSessionsChanged(viperManager: ViPERManager, sessions: List<Session>) {
            updateNotification(sessions = sessions)
        }
    }

    private val settingsListener = object : ViPERSettings.Listener {
        override fun onIsLegacyModeChanged(viperSettings: ViPERSettings, isLegacyMode: Boolean) {
            updateNotification()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand() called with: intent = $intent, flags = $flags, startId = $startId")
        
        startForeground(1, getNotification())

        when (intent?.action) {
            "android.media.action.OPEN_AUDIO_EFFECT_CONTROL_SESSION" -> {
                val packageName = intent.getStringExtra("android.media.extra.PACKAGE_NAME") ?: ""
                val sessionId = intent.getIntExtra("android.media.extra.AUDIO_SESSION", -1)
                val contentType = intent.getIntExtra("android.media.extra.CONTENT_TYPE", AudioEffect.CONTENT_TYPE_MUSIC)

                viperManager.addSession(packageName, sessionId, contentType)
            }
            "android.media.action.CLOSE_AUDIO_EFFECT_CONTROL_SESSION" -> {
                val packageName = intent.getStringExtra("android.media.extra.PACKAGE_NAME") ?: ""
                val sessionId = intent.getIntExtra("android.media.extra.AUDIO_SESSION", -1)

                viperManager.removeSession(packageName, sessionId)
            }
        }

        if (!viperManager.hasSessions()) {
            stopSelf(startId)
        }

        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        viperManager.addListener(managerListener)
        viperSettings.addListener(settingsListener)
    }

    override fun onDestroy() {
        viperManager.removeListener(managerListener)
        viperSettings.removeListener(settingsListener)
        super.onDestroy()
    }

    private fun updateNotification(route: MediaRouter.RouteInfo = viperManager.getSelectedMediaRoute(), sessions: List<Session> = viperManager.getCurrentSessions()) {
        val notification = getNotification(route, sessions)
        NotificationManagerCompat.from(this).notify(1, notification)
    }

    private fun getNotification(route: MediaRouter.RouteInfo = viperManager.getSelectedMediaRoute(), sessions: List<Session> = viperManager.getCurrentSessions()): Notification {
        val pendingIntent = packageManager.getLaunchIntentForPackage(packageName).let {
            PendingIntent.getActivity(this, 0, it, PendingIntent.FLAG_IMMUTABLE)
        }
        val text = if (viperSettings.isLegacyMode) "Legacy mode" else sessions.map {
            AndroidUtils.getApplicationLabel(this, it.packageName)
        }.distinct().joinToString().ifEmpty { "No active sessions" }

        return NotificationCompat.Builder(this, ViPERApp.SERVICES_CHANNEL_ID)
            .setContentTitle("${route.getDisplayName()} connected")
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setColor(Color.parseColor("#6100ED"))
            .setOngoing(true)
            .setShowWhen(false)
            .setOnlyAlertOnce(true)
            .build()
    }

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("This service does not support binding")
    }
}