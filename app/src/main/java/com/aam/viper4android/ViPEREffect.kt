package com.aam.viper4android

import com.aam.viper4android.ktx.AudioEffectKtx
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import java.util.*

class ViPEREffect(audioSession: Int) {
    companion object {
        private val UUID_NULL = UUID.fromString("ec7178ec-e5e1-4432-a3f4-4657e6795210")
        private val UUID_V4A = UUID.fromString("90380da3-8536-4744-a6a3-5731970e640f")
    }

    private val audioEffect = AudioEffectKtx(UUID_NULL, UUID_V4A, 0, audioSession)
    private val actor = AudioEffectActor(audioEffect)

    val status = Status()
    val masterLimiter = MasterLimiter()
    val playbackGainControl = PlaybackGainControl()
    val fetCompressor = FETCompressor()
    val viperDDC = ViPERDDC()
    val spectrumExtension = SpectrumExtension()
    val firEqualizer = FIREqualizer()
    val convolver = Convolver()
    val fieldSurround = FieldSurround()
    val differentialSurround = DifferentialSurround()
    val headphoneSurroundPlus = HeadphoneSurroundPlus()
    val reverberation = Reverberation()
    val dynamicSystem = DynamicSystem()
    val tubeSimulator = TubeSimulator()
    val viperBass = ViPERBass()
    val viperClarity = ViPERClarity()
    val auditorySystemProtection = AuditorySystemProtection()
    val analogX = AnalogX()
    val speakerOptimization = SpeakerOptimization()

    private fun getEnabled(result: CompletableDeferred<Boolean>) = actor.getEnabled(result)

    private fun getEnabledSync(): Boolean {
        val result = CompletableDeferred<Boolean>()
        getEnabled(result)
        return runBlocking { result.await() }
    }

    private fun setEnabled(enabled: Boolean) = actor.setEnabled(enabled)

    private fun getParameter(
        param: ByteArray,
        value: ByteArray,
        result: CompletableDeferred<Int>? = null
    ) = actor.getParameter(param, value, result)

    private fun getParameterSync(param: ByteArray, value: ByteArray): Int {
        val result = CompletableDeferred<Int>()
        getParameter(param, value, result)
        return runBlocking { result.await() }
    }

    private fun setParameter(
        param: ByteArray,
        value: ByteArray,
        result: CompletableDeferred<Int>? = null
    ) = actor.setParameter(param, value, result)

    fun release() {
        actor.close {
            audioEffect.release()
        }
    }

    inner class Status {

    }

    inner class MasterLimiter {
        var enabled: Boolean
            get() = getEnabledSync()
            set(value) {
                setEnabled(value)
            }

        var outputGain: Int
            get() {
                return 0
            } // TODO
            set(value) {} // TODO

        var outputPan: Int
            get() {
                return 0
            } // TODO
            set(value) {} // TODO

        var thresholdLimit: Int
            get() {
                return 0
            } // TODO
            set(value) {} // TODO
    }

    inner class PlaybackGainControl {
        var enabled: Boolean
            get() {
                return false
            } // TODO
            set(value) {} // TODO
    }

    inner class FETCompressor {
        var enabled: Boolean
            get() {
                return false
            } // TODO
            set(value) {} // TODO
    }

    inner class ViPERDDC {
        var enabled: Boolean
            get() {
                return false
            } // TODO
            set(value) {} // TODO
    }

    inner class SpectrumExtension {
        var enabled: Boolean
            get() {
                return false
            } // TODO
            set(value) {} // TODO

        var strength: Int
            get() {
                return 0
            } // TODO
            set(value) {} // TODO
    }

    inner class FIREqualizer {
        var enabled: Boolean
            get() {
                return false
            } // TODO
            set(value) {} // TODO
    }

    inner class Convolver {
        var enabled: Boolean
            get() {
                return false
            } // TODO
            set(value) {} // TODO
    }

    inner class FieldSurround {
        var enabled: Boolean
            get() {
                return false
            } // TODO
            set(value) {} // TODO

        var surroundStrength: Int
            get() {
                return 0
            } // TODO
            set(value) {} // TODO

        var midImageStrength: Int
            get() {
                return 0
            } // TODO
            set(value) {} // TODO
    }

    inner class DifferentialSurround {
        var enabled: Boolean
            get() {
                return false
            } // TODO
            set(value) {} // TODO

        var delay: Int
            get() {
                return 0
            } // TODO
            set(value) {} // TODO
    }

    inner class HeadphoneSurroundPlus {
        var enabled: Boolean
            get() {
                return false
            } // TODO
            set(value) {} // TODO
    }

    inner class Reverberation {
        var enabled: Boolean
            get() {
                return false
            } // TODO
            set(value) {} // TODO
    }

    inner class DynamicSystem {
        var enabled: Boolean
            get() {
                return false
            } // TODO
            set(value) {} // TODO

        var strength: Int
            get() {
                return 0
            } // TODO
            set(value) {} // TODO
    }

    inner class TubeSimulator {
        var enabled: Boolean
            get() {
                return false
            } // TODO
            set(value) {} // TODO
    }

    inner class ViPERBass {
        var enabled: Boolean
            get() {
                return false
            } // TODO
            set(value) {} // TODO
    }

    inner class ViPERClarity {
        var enabled: Boolean
            get() {
                return false
            } // TODO
            set(value) {} // TODO
    }

    inner class AuditorySystemProtection {
        var enabled: Boolean
            get() {
                return false
            } // TODO
            set(value) {} // TODO
    }

    inner class AnalogX {
        var enabled: Boolean
            get() {
                return false
            } // TODO
            set(value) {} // TODO
    }

    inner class SpeakerOptimization {
        var enabled: Boolean
            get() {
                return false
            } // TODO
            set(value) {} // TODO
    }
}