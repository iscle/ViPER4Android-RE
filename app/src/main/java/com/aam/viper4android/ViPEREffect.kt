package com.aam.viper4android

import com.aam.viper4android.ktx.AudioEffectKtx
import com.aam.viper4android.ktx.getBooleanParameter
import com.aam.viper4android.ktx.getByteArrayParameter
import com.aam.viper4android.ktx.getIntParameter
import com.aam.viper4android.ktx.getUByteParameter
import com.aam.viper4android.ktx.getUIntParameter
import com.aam.viper4android.ktx.getULongParameter
import java.util.UUID

class ViPEREffect(sessionId: Int) {
    private val audioEffect = AudioEffectKtx(UUID_NULL, VIPER_UUID, 0, sessionId)

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

    fun release() {
        audioEffect.release()
    }

    inner class Status {
        val enabled: Boolean
            get() = audioEffect.getBooleanParameter(PARAM_GET_ENABLED)

        val frameCount: ULong
            get() = audioEffect.getULongParameter(PARAM_GET_FRAME_COUNT)

        val version: UInt
            get() = audioEffect.getUIntParameter(PARAM_GET_VERSION)

        val disableReason: DisableReason
            get() = DisableReason.fromValue(audioEffect.getIntParameter(PARAM_GET_DISABLE_REASON))

        val config: Configs
            get() = Configs.fromBytes(audioEffect.getByteArrayParameter(PARAM_GET_CONFIG, 40))

        val architecture: Architecture
            get() = Architecture.fromValue(audioEffect.getUByteParameter(PARAM_GET_ARCHITECTURE))
    }

    inner class MasterLimiter {
        var enabled: Boolean
            get() = audioEffect.enabled
            set(value) { audioEffect.enabled = value }

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

    companion object {
        private val UUID_NULL = UUID.fromString("ec7178ec-e5e1-4432-a3f4-4657e6795210")
        val VIPER_UUID = UUID.fromString("90380da3-8536-4744-a6a3-5731970e640f")

        // typedef enum {
        //    PARAM_GET_ENABLED = 0,
        //    PARAM_GET_FRAME_COUNT,
        //    PARAM_GET_VERSION,
        //    PARAM_GET_DISABLE_REASON,
        //    PARAM_GET_CONFIG,
        //    PARAM_GET_ARCHITECTURE,
        //} param_get_t;
        private const val PARAM_GET_ENABLED = 0u
        private const val PARAM_GET_FRAME_COUNT = 1u
        private const val PARAM_GET_VERSION = 2u
        private const val PARAM_GET_DISABLE_REASON = 3u
        private const val PARAM_GET_CONFIG = 4u
        private const val PARAM_GET_ARCHITECTURE = 5u
    }
}