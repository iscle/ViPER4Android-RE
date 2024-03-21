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
import com.aam.viper4android.util.debounce
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
    private val scope = CoroutineScope(Dispatchers.IO)
    private val bootCount = getBootCount(context.contentResolver)
    private val mediaRouter = MediaRouter.getInstance(context)

    private var _currentRoute = MutableStateFlow(ViPERRoute.fromRouteInfo(mediaRouter.selectedRoute))
    val currentRoute = _currentRoute.asStateFlow()

    private var _currentPreset = MutableStateFlow(Preset())
    val currentPreset = _currentPreset.asStateFlow()

    private var _currentSessions = MutableStateFlow<List<Session>>(emptyList())
    val currentSessions = _currentSessions.asStateFlow()

    private val _enabled = MutableStateFlow(false)
    val enabled = _enabled.asStateFlow()
    val analogX = AnalogX()
    val auditorySystemProtection = AuditorySystemProtection()
    val convolver = Convolver()
    val differentialSurround = DifferentialSurround()
    val dynamicSystem = DynamicSystem()
    val fetCompressor = FETCompressor()
    val fieldSurroundEffect = FieldSurroundEffect()
    val firEqualizer = FIREqualizer()
    val headphoneSurroundPlus = HeadphoneSurroundPlus()
    val masterLimiter = MasterLimiter()
    val playbackGainControl = PlaybackGainControl()
    val reverberation = Reverberation()
    val speakerOptimization = SpeakerOptimization()
    val spectrumExtension = SpectrumExtension()
    val tubeSimulator6N1J = TubeSimulator6N1J()
    val viperBass = ViPERBass()
    val viperClarity = ViPERClarity()
    val viperDdc = ViPERDDC()

    // Internal variables
    private val sessions = mutableListOf<Session>()
    private var isReady = MutableStateFlow(false)

    init {
        scope.launch() {
            sessionDao.deleteObsolete(bootCount)
            observeMediaRouter()
            collectCurrentRoute()
            collectLegacyMode()
            isReady.value = true
        }
    }

    private fun observeMediaRouter() {
        scope.launch(Dispatchers.Main) {
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
                    .addControlCategory(MediaControlIntent.CATEGORY_LIVE_VIDEO)
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
            currentRoute.collect {
                val preset = getPresetForRoute(it)
                setPreset(preset)
            }
        }
    }

    private fun collectLegacyMode() {
        scope.launch {
            viperSettings.legacyMode.collect { legacyMode ->
                if (legacyMode) {
                    sessions.forEach { it.release() }
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
            sessions.add(Session(this, packageName, sessionId))
        } catch (e: Exception) {
            Log.e(TAG, "addSessionSafe: Failed to create session", e)
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
            session.release()
            sessions.remove(session)
            notifySessions()
        }
    }

    private fun setPreset(preset: Preset) {
        _currentPreset.value = preset

        setEnabled(preset.enabled)
        analogX.setEnabled(preset.analogX.enabled)
        analogX.setLevel(preset.analogX.level)
        auditorySystemProtection.setEnabled(preset.auditorySystemProtection.enabled)
        convolver.setEnabled(preset.convolver.enabled)
        differentialSurround.setEnabled(preset.differentialSurround.enabled)
        differentialSurround.setDelay(preset.differentialSurround.delay)
        dynamicSystem.setEnabled(preset.dynamicSystem.enabled)
        dynamicSystem.setDeviceType(preset.dynamicSystem.deviceType)
        dynamicSystem.setDynamicBassStrength(preset.dynamicSystem.dynamicBassStrength)
        fetCompressor.setEnabled(preset.fetCompressor.enabled)
        fieldSurroundEffect.setEnabled(preset.fieldSurround.enabled)
        fieldSurroundEffect.setSurroundStrength(preset.fieldSurround.surroundStrength)
        fieldSurroundEffect.setMidImageStrength(preset.fieldSurround.midImageStrength)
        firEqualizer.setEnabled(preset.firEqualizer.enabled)
        headphoneSurroundPlus.setEnabled(preset.headphoneSurroundPlus.enabled)
        masterLimiter.setOutputGain(preset.masterLimiter.outputGain)
        masterLimiter.setOutputPan(preset.masterLimiter.outputPan)
        masterLimiter.setThresholdLimit(preset.masterLimiter.thresholdLimit)
        playbackGainControl.setEnabled(preset.playbackGainControl.enabled)
        reverberation.setEnabled(preset.reverberation.enabled)
        speakerOptimization.setEnabled(preset.speakerOptimization.enabled)
        spectrumExtension.setEnabled(preset.spectrumExtension.enabled)
        spectrumExtension.setStrength(preset.spectrumExtension.strength)
        tubeSimulator6N1J.setEnabled(preset.tubeSimulator6N1J.enabled)
        viperBass.setEnabled(preset.viperBass.enabled)
        viperBass.setMode(preset.viperBass.mode)
        viperBass.setFrequency(preset.viperBass.frequency)
        viperBass.setGain(preset.viperBass.gain)
        viperClarity.setEnabled(preset.viperClarity.enabled)
        viperClarity.setMode(preset.viperClarity.mode)
        viperClarity.setGain(preset.viperClarity.gain)
        viperDdc.setEnabled(preset.viperDdc.enabled)
    }

    private val savePreset = debounce(5000, scope) {
        // todo
    }

    fun setEnabled(enabled: Boolean) {
        _enabled.value = enabled
        if (_currentPreset.value.enabled != enabled) {
            _currentPreset.value.enabled = enabled
            savePreset()
        }
    }

    inner class AnalogX {
        private val _enabled = MutableStateFlow(false)
        val enabled = _enabled.asStateFlow()

        private val _level = MutableStateFlow(0)
        val level = _level.asStateFlow()

        fun setEnabled(enabled: Boolean) {
            _enabled.value = enabled
            if (_currentPreset.value.analogX.enabled != enabled) {
                _currentPreset.value.analogX.enabled = enabled
                savePreset()
            }
        }

        fun setLevel(level: Int) {
            _level.value = level
            if (_currentPreset.value.analogX.level != level) {
                _currentPreset.value.analogX.level = level
                savePreset()
            }
        }
    }

    inner class AuditorySystemProtection {
        private val _enabled = MutableStateFlow(false)
        val enabled = _enabled.asStateFlow()

        fun setEnabled(enabled: Boolean) {
            _enabled.value = enabled
            if (_currentPreset.value.auditorySystemProtection.enabled != enabled) {
                _currentPreset.value.auditorySystemProtection.enabled = enabled
                savePreset()
            }
        }
    }

    inner class Convolver {
        private val _enabled = MutableStateFlow(false)
        val enabled = _enabled.asStateFlow()

        fun setEnabled(enabled: Boolean) {
            _enabled.value = enabled
            if (_currentPreset.value.convolver.enabled != enabled) {
                _currentPreset.value.convolver.enabled = enabled
                savePreset()
            }
        }
    }

    inner class DifferentialSurround {
        private val _enabled = MutableStateFlow(false)
        val enabled = _enabled.asStateFlow()

        private val _delay = MutableStateFlow<UShort>(4u)
        val delay = _delay.asStateFlow()

        fun setEnabled(enabled: Boolean) {
            _enabled.value = enabled
            if (_currentPreset.value.differentialSurround.enabled != enabled) {
                _currentPreset.value.differentialSurround.enabled = enabled
                savePreset()
            }
        }

        fun setDelay(delay: UShort) {
            _delay.value = delay
            if (_currentPreset.value.differentialSurround.delay != delay) {
                _currentPreset.value.differentialSurround.delay = delay
                savePreset()
            }
        }
    }

    inner class DynamicSystem {
        private val _enabled = MutableStateFlow(false)
        val enabled = _enabled.asStateFlow()

        private val _deviceType = MutableStateFlow(0)
        val deviceType = _deviceType.asStateFlow()

        private val _dynamicBassStrength = MutableStateFlow(0)
        val dynamicBassStrength = _dynamicBassStrength.asStateFlow()

        fun setEnabled(enabled: Boolean) {
            _enabled.value = enabled
            if (_currentPreset.value.dynamicSystem.enabled != enabled) {
                _currentPreset.value.dynamicSystem.enabled = enabled
                savePreset()
            }
        }

        fun setDeviceType(deviceType: Int) {
            _deviceType.value = deviceType
            if (_currentPreset.value.dynamicSystem.deviceType != deviceType) {
                _currentPreset.value.dynamicSystem.deviceType = deviceType
                savePreset()
            }
        }

        fun setDynamicBassStrength(dynamicBassStrength: Int) {
            _dynamicBassStrength.value = dynamicBassStrength
            if (_currentPreset.value.dynamicSystem.dynamicBassStrength != dynamicBassStrength) {
                _currentPreset.value.dynamicSystem.dynamicBassStrength = dynamicBassStrength
                savePreset()
            }
        }
    }

    inner class FETCompressor {
        private val _enabled = MutableStateFlow(false)
        val enabled = _enabled.asStateFlow()

        fun setEnabled(enabled: Boolean) {
            _enabled.value = enabled
            if (_currentPreset.value.fetCompressor.enabled != enabled) {
                _currentPreset.value.fetCompressor.enabled = enabled
                savePreset()
            }
        }
    }

    inner class FieldSurroundEffect {
        private val _enabled = MutableStateFlow(false)
        val enabled = _enabled.asStateFlow()

        private val _surroundStrength = MutableStateFlow(0)
        val surroundStrength = _surroundStrength.asStateFlow()

        private val _midImageStrength = MutableStateFlow(5)
        val midImageStrength = _midImageStrength.asStateFlow()

        fun setEnabled(enabled: Boolean) {
            _enabled.value = enabled
            if (_currentPreset.value.fieldSurround.enabled != enabled) {
                _currentPreset.value.fieldSurround.enabled = enabled
                savePreset()
            }
        }

        fun setSurroundStrength(surroundStrength: Int) {
            _surroundStrength.value = surroundStrength
            if (_currentPreset.value.fieldSurround.surroundStrength != surroundStrength) {
                _currentPreset.value.fieldSurround.surroundStrength = surroundStrength
                savePreset()
            }
        }

        fun setMidImageStrength(midImageStrength: Int) {
            _midImageStrength.value = midImageStrength
            if (_currentPreset.value.fieldSurround.midImageStrength != midImageStrength) {
                _currentPreset.value.fieldSurround.midImageStrength = midImageStrength
                savePreset()
            }
        }
    }

    inner class FIREqualizer {
        private val _enabled = MutableStateFlow(false)
        val enabled = _enabled.asStateFlow()

        fun setEnabled(enabled: Boolean) {
            _enabled.value = enabled
            if (_currentPreset.value.firEqualizer.enabled != enabled) {
                _currentPreset.value.firEqualizer.enabled = enabled
                savePreset()
            }
        }
    }

    inner class HeadphoneSurroundPlus {
        private val _enabled = MutableStateFlow(false)
        val enabled = _enabled.asStateFlow()

        private val _level = MutableStateFlow<UByte>(0u)
        val level = _level.asStateFlow()

        fun setEnabled(enabled: Boolean) {
            _enabled.value = enabled
            if (_currentPreset.value.headphoneSurroundPlus.enabled != enabled) {
                _currentPreset.value.headphoneSurroundPlus.enabled = enabled
                savePreset()
            }
        }

        fun setLevel(level: UByte) {
            _level.value = level
            if (_currentPreset.value.headphoneSurroundPlus.level != level) {
                _currentPreset.value.headphoneSurroundPlus.level = level
                savePreset()
            }
        }
    }

    inner class MasterLimiter {
        private val _outputGain = MutableStateFlow<UByte>(100u)
        val outputGain = _outputGain.asStateFlow()

        private val _outputPan = MutableStateFlow<UByte>(50u)
        val outputPan = _outputPan.asStateFlow()

        private val _thresholdLimit = MutableStateFlow<UByte>(100u)
        val thresholdLimit = _thresholdLimit.asStateFlow()

        fun setOutputGain(outputGain: UByte) {
            _outputGain.value = outputGain
            if (_currentPreset.value.masterLimiter.outputGain != outputGain) {
                _currentPreset.value.masterLimiter.outputGain = outputGain
                savePreset()
            }
        }

        fun setOutputPan(outputPan: UByte) {
            _outputPan.value = outputPan
            if (_currentPreset.value.masterLimiter.outputPan != outputPan) {
                _currentPreset.value.masterLimiter.outputPan = outputPan
                savePreset()
            }
        }

        fun setThresholdLimit(thresholdLimit: UByte) {
            _thresholdLimit.value = thresholdLimit
            if (_currentPreset.value.masterLimiter.thresholdLimit != thresholdLimit) {
                _currentPreset.value.masterLimiter.thresholdLimit = thresholdLimit
                savePreset()
            }
        }
    }

    inner class PlaybackGainControl {
        private val _enabled = MutableStateFlow(false)
        val enabled = _enabled.asStateFlow()

        fun setEnabled(enabled: Boolean) {
            _enabled.value = enabled
            if (_currentPreset.value.playbackGainControl.enabled != enabled) {
                _currentPreset.value.playbackGainControl.enabled = enabled
                savePreset()
            }
        }
    }

    inner class Reverberation {
        private val _enabled = MutableStateFlow(false)
        val enabled = _enabled.asStateFlow()

        private val _roomSize = MutableStateFlow<UByte>(0u)
        val roomSize = _roomSize.asStateFlow()

        private val _soundField = MutableStateFlow<UByte>(0u)
        val soundField = _soundField.asStateFlow()

        private val _damping = MutableStateFlow<UByte>(0u)
        val damping = _damping.asStateFlow()

        private val _wetSignal = MutableStateFlow<UByte>(0u)
        val wetSignal = _wetSignal.asStateFlow()

        private val _drySignal = MutableStateFlow<UByte>(50u)
        val drySignal = _drySignal.asStateFlow()

        fun setEnabled(enabled: Boolean) {
            _enabled.value = enabled
            if (_currentPreset.value.reverberation.enabled != enabled) {
                _currentPreset.value.reverberation.enabled = enabled
                savePreset()
            }
        }

        fun setRoomSize(roomSize: UByte) {
            _roomSize.value = roomSize
            if (_currentPreset.value.reverberation.roomSize != roomSize) {
                _currentPreset.value.reverberation.roomSize = roomSize
                savePreset()
            }
        }

        fun setSoundField(soundField: UByte) {
            _soundField.value = soundField
            if (_currentPreset.value.reverberation.soundField != soundField) {
                _currentPreset.value.reverberation.soundField = soundField
                savePreset()
            }
        }

        fun setDamping(damping: UByte) {
            _damping.value = damping
            if (_currentPreset.value.reverberation.damping != damping) {
                _currentPreset.value.reverberation.damping = damping
                savePreset()
            }
        }

        fun setWetSignal(wetSignal: UByte) {
            _wetSignal.value = wetSignal
            if (_currentPreset.value.reverberation.wetSignal != wetSignal) {
                _currentPreset.value.reverberation.wetSignal = wetSignal
                savePreset()
            }
        }

        fun setDrySignal(drySignal: UByte) {
            _drySignal.value = drySignal
            if (_currentPreset.value.reverberation.drySignal != drySignal) {
                _currentPreset.value.reverberation.drySignal = drySignal
                savePreset()
            }
        }
    }

    inner class SpeakerOptimization {
        private val _enabled = MutableStateFlow(false)
        val enabled = _enabled.asStateFlow()

        fun setEnabled(enabled: Boolean) {
            _enabled.value = enabled
            if (_currentPreset.value.speakerOptimization.enabled != enabled) {
                _currentPreset.value.speakerOptimization.enabled = enabled
                savePreset()
            }
        }
    }

    inner class SpectrumExtension {
        private val _enabled = MutableStateFlow(false)
        val enabled = _enabled.asStateFlow()

        private val _strength = MutableStateFlow<UByte>(10u)
        val strength = _strength.asStateFlow()

        fun setEnabled(enabled: Boolean) {
            _enabled.value = enabled
            if (_currentPreset.value.spectrumExtension.enabled != enabled) {
                _currentPreset.value.spectrumExtension.enabled = enabled
                savePreset()
            }
        }

        fun setStrength(strength: UByte) {
            _strength.value = strength
            if (_currentPreset.value.spectrumExtension.strength != strength) {
                _currentPreset.value.spectrumExtension.strength = strength
                savePreset()
            }
        }
    }

    inner class TubeSimulator6N1J {
        private val _enabled = MutableStateFlow(false)
        val enabled = _enabled.asStateFlow()

        fun setEnabled(enabled: Boolean) {
            _enabled.value = enabled
            if (_currentPreset.value.tubeSimulator6N1J.enabled != enabled) {
                _currentPreset.value.tubeSimulator6N1J.enabled = enabled
                savePreset()
            }
        }
    }

    inner class ViPERBass {
        private val _enabled = MutableStateFlow(false)
        val enabled = _enabled.asStateFlow()

        private val _mode = MutableStateFlow<UByte>(0u)
        val mode = _mode.asStateFlow()

        private val _frequency = MutableStateFlow<UByte>(15u)
        val frequency = _frequency.asStateFlow()

        private val _gain = MutableStateFlow<UShort>(50u)
        val gain = _gain.asStateFlow()

        fun setEnabled(enabled: Boolean) {
            _enabled.value = enabled
            if (_currentPreset.value.viperBass.enabled != enabled) {
                _currentPreset.value.viperBass.enabled = enabled
                savePreset()
            }
        }

        fun setMode(mode: UByte) {
            _mode.value = mode
            if (_currentPreset.value.viperBass.mode != mode) {
                _currentPreset.value.viperBass.mode = mode
                savePreset()
            }
        }

        fun setFrequency(frequency: UByte) {
            _frequency.value = frequency
            if (_currentPreset.value.viperBass.frequency != frequency) {
                _currentPreset.value.viperBass.frequency = frequency
                savePreset()
            }
        }

        fun setGain(bassGain: UShort) {
            _gain.value = bassGain
            if (_currentPreset.value.viperBass.gain != bassGain) {
                _currentPreset.value.viperBass.gain = bassGain
                savePreset()
            }
        }
    }

    inner class ViPERClarity {
        private val _enabled = MutableStateFlow(false)
        val enabled = _enabled.asStateFlow()

        private val _mode = MutableStateFlow<UByte>(0u)
        val mode = _mode.asStateFlow()

        private val _gain = MutableStateFlow<UShort>(1u)
        val gain = _gain.asStateFlow()

        fun setEnabled(enabled: Boolean) {
            _enabled.value = enabled
            if (_currentPreset.value.viperClarity.enabled != enabled) {
                _currentPreset.value.viperClarity.enabled = enabled
                savePreset()
            }
        }

        fun setMode(mode: UByte) {
            _mode.value = mode
            if (_currentPreset.value.viperClarity.mode != mode) {
                _currentPreset.value.viperClarity.mode = mode
                savePreset()
            }
        }

        fun setGain(gain: UShort) {
            _gain.value = gain
            if (_currentPreset.value.viperClarity.gain != gain) {
                _currentPreset.value.viperClarity.gain = gain
                savePreset()
            }
        }
    }

    inner class ViPERDDC {
        private val _enabled = MutableStateFlow(false)
        val enabled = _enabled.asStateFlow()

        fun setEnabled(enabled: Boolean) {
            _enabled.value = enabled
            if (_currentPreset.value.viperDdc.enabled != enabled) {
                _currentPreset.value.viperDdc.enabled = enabled
                savePreset()
            }
        }
    }

    companion object {
        private const val TAG = "ViPERManager"

        val isViperAvailable by lazy {
            AudioEffect.queryEffects()
                ?.any { it.uuid == ViPEREffect.VIPER_UUID } == true
        }
    }
}