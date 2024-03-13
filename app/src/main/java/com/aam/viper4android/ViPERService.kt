package com.aam.viper4android

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.Color
import android.media.audiofx.AudioEffect
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.aam.viper4android.persistence.ViPERSettings
import com.aam.viper4android.util.AndroidUtils
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ViPERService : LifecycleService() {
    @Inject lateinit var viperManager: ViPERManager
    @Inject lateinit var viperSettings: ViPERSettings

    private var isForeground = false
    private val intentsFlow = MutableSharedFlow<Intent>(
        extraBufferCapacity = Int.MAX_VALUE,
    )

    // todo: add callback or something to update the notification when a session is added or removed or legacy mode is toggled

    override fun onCreate() {
        super.onCreate()
        collectIntents()
        collectSessions()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        if (!isForeground) {
            try {
                ServiceCompat.startForeground(
                    this,
                    SERVICE_NOTIFICATION_ID,
                    getNotification(),
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
                )
                isForeground = true
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
            }
        }

        if (intent != null) {
            intentsFlow.tryEmit(intent)
        }

        return START_STICKY
    }

    private fun collectIntents() {
        lifecycleScope.launch {
            intentsFlow.collect(::handleIntent)
        }
    }

    private suspend fun handleIntent(intent: Intent) {
        when (intent.action) {
            AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION -> {
                val packageName = intent.getStringExtra(AudioEffect.EXTRA_PACKAGE_NAME)
                val sessionId = intent.getIntExtra(AudioEffect.EXTRA_AUDIO_SESSION, -1)
                if (packageName == null || sessionId == -1) return
                viperManager.addSession(packageName, sessionId)
            }
            AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION -> {
                val packageName = intent.getStringExtra(AudioEffect.EXTRA_PACKAGE_NAME)
                val sessionId = intent.getIntExtra(AudioEffect.EXTRA_AUDIO_SESSION, -1)
                if (packageName == null || sessionId == -1) return
                viperManager.removeSession(packageName, sessionId)
            }
        }
    }

    private fun collectSessions() {
        lifecycleScope.launch {
            viperManager.waitForReady()
            viperManager.currentSessions.collect { sessions ->
                if (sessions.isEmpty()) {
                    stopSelf()
                } else {
                    updateNotification()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateNotification() {
        NotificationManagerCompat.from(this)
            .notify(SERVICE_NOTIFICATION_ID, getNotification())
    }

    private fun getNotification(): Notification {
        val pendingIntent = packageManager.getLaunchIntentForPackage(packageName).let {
            PendingIntent.getActivity(this, 0, it, PendingIntent.FLAG_IMMUTABLE)
        }

        val title = getString(
            R.string.device_connected,
            viperManager.currentRoute.value.getName()
        )

        val text = if (viperSettings.isLegacyMode)
            getString(R.string.legacy_mode)
        else
            getSessionAppLabelsString()

        return NotificationCompat.Builder(this, ViPERApp.SERVICES_CHANNEL_ID)
            .setContentTitle(title)
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

    private fun getSessionAppLabelsString(): String {
        val sessions = viperManager.currentSessions.value
        return sessions.distinctBy { it.packageName }.map {
            AndroidUtils.getApplicationLabel(this, it.packageName)
        }.distinct().joinToString().ifEmpty { getString(R.string.no_active_sessions) }
    }

    companion object {
        private const val SERVICE_NOTIFICATION_ID = 1
    }
}