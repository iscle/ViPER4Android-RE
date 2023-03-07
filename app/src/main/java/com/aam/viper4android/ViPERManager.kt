package com.aam.viper4android

import android.content.Context
import android.media.audiofx.AudioEffect
import android.provider.Settings
import android.util.Log
import androidx.mediarouter.media.MediaControlIntent
import androidx.mediarouter.media.MediaRouteSelector
import androidx.mediarouter.media.MediaRouter
import com.aam.viper4android.persistence.ViPERDatabase
import com.aam.viper4android.persistence.ViPERSettings
import com.aam.viper4android.persistence.actor.SessionsDaoActor
import com.aam.viper4android.persistence.model.PersistedSession
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViPERManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val viperSettings: ViPERSettings,
    private val viperDatabase: ViPERDatabase,
    private val sessionsDaoActor: SessionsDaoActor
) {
    private val TAG = "ViPERManager"
    private val mediaRouter = MediaRouter.getInstance(context)

    private val isViperAvailable =
        AudioEffect.queryEffects()?.any { it.uuid == ViPEREffect.VIPER_UUID } ?: false
    private val bootCount =
        Settings.Global.getInt(context.contentResolver, Settings.Global.BOOT_COUNT)

    private val sessions = mutableListOf<Session>()
    private val listeners = mutableListOf<Listener>()

    private val mediaRouterCallback = object : MediaRouter.Callback() {
        override fun onRouteSelected(
            router: MediaRouter,
            route: MediaRouter.RouteInfo,
            reason: Int
        ) {
            listeners.forEach { it.onSelectedMediaRouteChanged(this@ViPERManager, route) }
        }

        override fun onRouteChanged(router: MediaRouter, route: MediaRouter.RouteInfo) {
            if (route == mediaRouter.selectedRoute) {
                listeners.forEach { it.onSelectedMediaRouteChanged(this@ViPERManager, route) }
            }
        }
    }

    private val settingsListener = object : ViPERSettings.Listener {
        override fun onIsLegacyModeChanged(viperSettings: ViPERSettings, isLegacyMode: Boolean) {
            if (isLegacyMode) {
                sessions.forEach { it.release() }
                sessions.clear()
                addSessionSafe(context.packageName, 0, AudioEffect.CONTENT_TYPE_MUSIC)
                listeners.forEach { it.onSessionsChanged(this@ViPERManager, getCurrentSessions()) }
            } else {
                sessions.clear()
                CoroutineScope(Dispatchers.IO).launch {
                    restoreDatabaseSessions()
                    withContext(Dispatchers.Main) {
                        listeners.forEach {
                            it.onSessionsChanged(
                                this@ViPERManager,
                                getCurrentSessions()
                            )
                        }
                    }
                }
            }
        }
    }

    init {
        runBlocking {
            viperDatabase.sessionsDao().deleteObsolete(bootCount)
            if (!viperSettings.isLegacyMode) {
                restoreDatabaseSessions()
            }
        }
        viperSettings.addListener(settingsListener)
    }

    private suspend fun restoreDatabaseSessions() {
        viperDatabase.sessionsDao().getAll().forEach {
            addSessionSafe(it.packageName, it.sessionId, it.contentType)
        }
    }

    private fun addSessionSafe(packageName: String, sessionId: Int, contentType: Int) {
        try {
            sessions.add(Session(packageName, sessionId, contentType))
        } catch (e: Exception) {
            Log.e(TAG, "addSessionSafe: Failed to create Session", e)
        }
    }

    fun getIsViperAvailable(): Boolean {
        return isViperAvailable
    }

    fun hasSessions(): Boolean {
        return sessions.isNotEmpty()
    }

    fun getSelectedMediaRoute(): MediaRouter.RouteInfo {
        return mediaRouter.selectedRoute
    }

    fun addSession(packageName: String, sessionId: Int, contentType: Int) {
        sessionsDaoActor.insert(PersistedSession(packageName, sessionId, contentType, bootCount))
        if (viperSettings.isLegacyMode) return
        if (sessions.find { it.packageName == packageName && it.sessionId == sessionId } != null) return
        addSessionSafe(packageName, sessionId, contentType)
        listeners.forEach { it.onSessionsChanged(this, getCurrentSessions()) }
    }

    fun removeSession(packageName: String, sessionId: Int) {
        sessionsDaoActor.delete(packageName, sessionId)
        val session = sessions.find { it.packageName == packageName && it.sessionId == sessionId }
        if (session != null) {
            sessions.remove(session)
            listeners.forEach { it.onSessionsChanged(this, getCurrentSessions()) }
        }
    }

    fun getCurrentSessions(): List<Session> {
        return sessions
    }

    fun addListener(listener: Listener) {
        if (listeners.contains(listener)) return
        if (listeners.isEmpty()) {
            val selector = MediaRouteSelector.Builder()
                .addControlCategory(MediaControlIntent.CATEGORY_LIVE_AUDIO)
                .build()
            mediaRouter.addCallback(
                selector,
                mediaRouterCallback,
                MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY
            )
        }
        listeners.add(listener)
    }

    fun removeListener(listener: Listener) {
        if (!listeners.contains(listener)) return

        listeners.remove(listener)
        if (listeners.isEmpty()) {
            mediaRouter.removeCallback(mediaRouterCallback)
        }
    }

    interface Listener {
        fun onSelectedMediaRouteChanged(viperManager: ViPERManager, route: MediaRouter.RouteInfo)
        fun onSessionsChanged(viperManager: ViPERManager, sessions: List<Session>)
    }
}