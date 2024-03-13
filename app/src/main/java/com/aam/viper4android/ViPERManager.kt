package com.aam.viper4android

import android.content.Context
import android.media.audiofx.AudioEffect
import android.util.Log
import androidx.mediarouter.media.MediaControlIntent
import androidx.mediarouter.media.MediaRouteSelector
import androidx.mediarouter.media.MediaRouter
import com.aam.viper4android.ktx.asPreset
import com.aam.viper4android.ktx.getBootCount
import com.aam.viper4android.persistence.PresetsDao
import com.aam.viper4android.persistence.SessionDao
import com.aam.viper4android.persistence.ViPERSettings
import com.aam.viper4android.persistence.model.PersistedSession
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViPERManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val viperSettings: ViPERSettings,
    private val sessionDao: SessionDao,
    private val presetDao: PresetsDao,
) {
    private val job = Job()
    private val scope = CoroutineScope(job)
    private val bootCount = getBootCount(context.contentResolver)
    private val mediaRouter = MediaRouter.getInstance(context)

    private var _currentRoute = MutableStateFlow(ViPERRoute.fromRouteInfo(mediaRouter.selectedRoute))
    val currentRoute = _currentRoute.asStateFlow()

    private var _currentPreset = MutableStateFlow(Preset())
    val currentPreset = _currentPreset.asStateFlow()

    private var _currentSessions = MutableStateFlow<List<Session>>(emptyList())
    val currentSessions = _currentSessions.asStateFlow()

    // Internal variables
    private val sessions = mutableListOf<Session>()
    private var isReady = MutableStateFlow(false)

    init {
        scope.launch(Dispatchers.IO) {
            sessionDao.deleteObsolete(bootCount)
            observeMediaRouter()
            collectCurrentRoute()
            collectLegacyMode()
            isReady.value = true
        }
    }

    private fun observeMediaRouter() {
        scope.launch {
            callbackFlow {
                val callback = object : MediaRouter.Callback() {
                    override fun onRouteSelected(
                        router: MediaRouter,
                        route: MediaRouter.RouteInfo,
                        reason: Int
                    ) {
                        trySend(route)
                    }

                    override fun onRouteRemoved(router: MediaRouter, route: MediaRouter.RouteInfo) {
                        if (router.selectedRoute == route) {
                            router.unselect(MediaRouter.UNSELECT_REASON_UNKNOWN)
                        }
                    }
                }

                val selector = MediaRouteSelector.Builder()
                    .addControlCategory(MediaControlIntent.CATEGORY_LIVE_AUDIO)
                    .build()
                mediaRouter.addCallback(selector, callback, 0)

                // Update the current route immediately
                trySend(mediaRouter.selectedRoute)

                awaitClose {
                    mediaRouter.removeCallback(callback)
                }
            }.collect {
                // TODO: When adding Android Auto support, only update if not in Android Auto mode!
                _currentRoute.value = ViPERRoute.fromRouteInfo(it)
            }
        }
    }

    private fun collectCurrentRoute() {
        scope.launch {
            _currentRoute.collect {
                _currentPreset.value = getPresetForRoute(it)
            }
        }
    }

    private fun collectLegacyMode() {
        scope.launch {
            viperSettings.legacyMode.collect { legacyMode ->
                if (legacyMode) {
                    sessions.forEach { it.effect.release() }
                    sessions.clear()
                    addSessionSafe(context.packageName, 0)
                    notifySessions()
                } else {
                    sessions.clear()
                    sessionDao.getAll().forEach {
                        addSessionSafe(it.packageName, it.sessionId)
                    }
                    notifySessions()
                }
            }
        }
    }

    suspend fun waitForReady() {
        isReady.first { it }
    }

    private fun notifySessions() {
        _currentSessions.value = sessions.toList()
    }

    private suspend fun getPresetForRoute(route: ViPERRoute): Preset {
        return withContext(Dispatchers.IO) {
            presetDao.get(route.getId())?.asPreset()
        } ?: Preset()
    }

    private fun addSessionSafe(packageName: String, sessionId: Int) {
        try {
            sessions.add(Session(packageName, sessionId))
        } catch (e: Exception) {
            Log.e(TAG, "addSessionSafe: Failed to create Session", e)
        }
    }

    suspend fun addSession(packageName: String, sessionId: Int) {
        waitForReady()
        sessionDao.insert(PersistedSession(packageName, sessionId, bootCount))
        if (viperSettings.isLegacyMode) return
        if (sessions.find { it.packageName == packageName && it.sessionId == sessionId } != null) return
        addSessionSafe(packageName, sessionId)
        notifySessions()
    }

    suspend fun removeSession(packageName: String, sessionId: Int) {
        waitForReady()
        sessionDao.delete(packageName, sessionId)
        val session = sessions.find { it.packageName == packageName && it.sessionId == sessionId }
        if (session != null) {
            sessions.remove(session)
            notifySessions()
        }
    }

    companion object {
        private const val TAG = "ViPERManager"

        fun isViperAvailable(): Boolean {
            return AudioEffect.queryEffects()
                ?.any { it.uuid == ViPEREffect.VIPER_UUID } == true
        }
    }
}