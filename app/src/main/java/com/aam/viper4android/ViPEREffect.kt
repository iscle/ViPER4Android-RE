package com.aam.viper4android

import com.aam.viper4android.ktx.AudioEffectKtx
import com.aam.viper4android.ktx.getBooleanParameter
import com.aam.viper4android.ktx.getByteArrayParameter
import com.aam.viper4android.ktx.getIntParameter
import com.aam.viper4android.ktx.getUByteParameter
import com.aam.viper4android.ktx.getUIntParameter
import com.aam.viper4android.ktx.getULongParameter
import com.aam.viper4android.ktx.putUByte
import com.aam.viper4android.ktx.setBooleanParameter
import com.aam.viper4android.ktx.setByteArrayParameter
import com.aam.viper4android.ktx.setUByteArrayParameter
import com.aam.viper4android.ktx.setUByteParameter
import com.aam.viper4android.ktx.setUShortParameter
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.UUID

class ViPEREffect(sessionId: Int) {
    val audioEffect = AudioEffectKtx(UUID_NULL, VIPER_UUID, 0, sessionId)

    val status = Status()
    val masterLimiter = MasterLimiter()
    val playbackGainControl = PlaybackGainControl()
    val fetCompressor = FETCompressor()
    val viperDDC = ViPERDDC()
    val spectrumExtension = SpectrumExtension()
    val iirEqualizer = IIREqualizer()
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

    fun reset() = audioEffect.setBooleanParameter(PARAM_SET_RESET, true)

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
        @OptIn(ExperimentalUnsignedTypes::class)
        fun setOutputGain(gainL: UByte, gainR: UByte) = audioEffect.setUByteArrayParameter(PARAM_SET_OUTPUT_GAIN, ubyteArrayOf(gainL, gainR))
        fun setThresholdLimit(thresholdLimit: UByte) = audioEffect.setUByteParameter(PARAM_SET_THRESHOLD_LIMIT, thresholdLimit)
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
        fun setEnabled(enabled: Boolean) = audioEffect.setBooleanParameter(PARAM_SET_SPECTRUM_EXTENSION_ENABLE, enabled)
        fun setStrength(strength: UByte) = audioEffect.setUByteParameter(PARAM_SET_SPECTRUM_EXTENSION_STRENGTH, strength)
    }

    inner class IIREqualizer {
        fun setEnabled(enabled: Boolean) = audioEffect.setBooleanParameter(PARAM_SET_IIR_EQUALIZER_ENABLE, enabled)
        fun setBandLevel(band: UByte, level: Short) {
            val array = ByteBuffer.allocate(3).order(ByteOrder.nativeOrder())
                .putUByte(band)
                .putShort(level)
                .array()
            audioEffect.setByteArrayParameter(PARAM_SET_IIR_EQUALIZER_BAND_LEVEL, array)
        }
    }

    inner class Convolver {
        var enabled: Boolean
            get() {
                return false
            } // TODO
            set(value) {} // TODO
    }

    inner class FieldSurround {
        fun setEnabled(enabled: Boolean) = audioEffect.setBooleanParameter(PARAM_SET_FIELD_SURROUND_ENABLE, enabled)
        fun setDepth(depth: UShort) = audioEffect.setUShortParameter(PARAM_SET_FIELD_SURROUND_DEPTH, depth)
        fun setMidImage(midImage: UByte) = audioEffect.setUByteParameter(PARAM_SET_FIELD_SURROUND_MID_IMAGE, midImage)
    }

    inner class DifferentialSurround {
        fun setEnabled(enabled: Boolean) = audioEffect.setBooleanParameter(PARAM_SET_DIFFERENTIAL_SURROUND_ENABLE, enabled)
        fun setDelay(delay: UShort) = audioEffect.setUShortParameter(PARAM_SET_DIFFERENTIAL_SURROUND_DELAY, delay)
    }

    inner class HeadphoneSurroundPlus {
        fun setEnabled(enabled: Boolean) = audioEffect.setBooleanParameter(PARAM_SET_HEADPHONE_SURROUND_ENABLE, enabled)
        fun setLevel(level: UByte) = audioEffect.setUByteParameter(PARAM_SET_HEADPHONE_SURROUND_LEVEL, level)
    }

    inner class Reverberation {
        fun setEnabled(enabled: Boolean) = audioEffect.setBooleanParameter(PARAM_SET_REVERBERATION_ENABLE, enabled)
        fun setRoomSize(roomSize: UByte) = audioEffect.setUByteParameter(PARAM_SET_REVERBERATION_ROOM_SIZE, roomSize)
        fun setSoundField(soundField: UByte) = audioEffect.setUByteParameter(PARAM_SET_REVERBERATION_SOUND_FIELD, soundField)
        fun setDamping(damping: UByte) = audioEffect.setUByteParameter(PARAM_SET_REVERBERATION_DAMPING, damping)
        fun setWetSignal(wetSignal: UByte) = audioEffect.setUByteParameter(PARAM_SET_REVERBERATION_WET_SIGNAL, wetSignal)
        fun setDrySignal(drySignal: UByte) = audioEffect.setUByteParameter(PARAM_SET_REVERBERATION_DRY_SIGNAL, drySignal)
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
        fun setEnabled(enabled: Boolean) = audioEffect.setBooleanParameter(PARAM_SET_TUBE_SIMULATOR_ENABLE, enabled)
    }

    inner class ViPERBass {
        fun setEnabled(enabled: Boolean) = audioEffect.setBooleanParameter(PARAM_SET_VIPER_BASS_ENABLE, enabled)
        fun setMode(mode: UByte) = audioEffect.setUByteParameter(PARAM_SET_VIPER_BASS_MODE, mode)
        fun setFrequency(frequency: UByte) = audioEffect.setUByteParameter(PARAM_SET_VIPER_BASS_FREQUENCY, frequency)
        fun setGain(gain: UShort) = audioEffect.setUShortParameter(PARAM_SET_VIPER_BASS_GAIN, gain)
    }

    inner class ViPERClarity {
        fun setEnabled(enabled: Boolean) = audioEffect.setBooleanParameter(PARAM_SET_VIPER_CLARITY_ENABLE, enabled)
        fun setMode(mode: UByte) = audioEffect.setUByteParameter(PARAM_SET_VIPER_CLARITY_MODE, mode)
        fun setGain(gain: UShort) = audioEffect.setUShortParameter(PARAM_SET_VIPER_CLARITY_GAIN, gain)
    }

    inner class AuditorySystemProtection {
        fun setEnabled(enabled: Boolean) = audioEffect.setBooleanParameter(PARAM_SET_CURE_ENABLE, enabled)
        fun setLevel(level: UByte) = audioEffect.setUByteParameter(PARAM_SET_CURE_LEVEL, level)
    }

    inner class AnalogX {
        fun setEnabled(enabled: Boolean) = audioEffect.setBooleanParameter(PARAM_SET_ANALOGX_ENABLE, enabled)
        fun setLevel(level: UByte) = audioEffect.setUByteParameter(PARAM_SET_ANALOGX_LEVEL, level)
    }

    inner class SpeakerOptimization {
        fun setEnabled(enabled: Boolean) = audioEffect.setBooleanParameter(PARAM_SET_SPEAKER_OPTIMIZATION_ENABLE, enabled)
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

        // typedef enum {
        //    PARAM_SET_RESET = 0,
        //    PARAM_SET_DDC_ENABLE,
        //    PARAM_SET_DDC_COEFFICIENTS,
        //    PARAM_SET_VIPER_BASS_ENABLE,
        //    PARAM_SET_VIPER_BASS_MODE,
        //    PARAM_SET_VIPER_BASS_FREQUENCY,
        //    PARAM_SET_VIPER_BASS_GAIN,
        //    PARAM_SET_VIPER_CLARITY_ENABLE,
        //    PARAM_SET_VIPER_CLARITY_MODE,
        //    PARAM_SET_VIPER_CLARITY_GAIN,
        //    PARAM_SET_OUTPUT_GAIN,
        //    PARAM_SET_THRESHOLD_LIMIT,
        //    PARAM_SET_SPEAKER_OPTIMIZATION_ENABLE,
        //    PARAM_SET_ANALOGX_ENABLE,
        //    PARAM_SET_ANALOGX_LEVEL,
        //    PARAM_SET_TUBE_SIMULATOR_ENABLE,
        //    PARAM_SET_CURE_ENABLE,
        //    PARAM_SET_CURE_LEVEL,
        //    PARAM_SET_REVERBERATION_ENABLE,
        //    PARAM_SET_REVERBERATION_ROOM_SIZE,
        //    PARAM_SET_REVERBERATION_SOUND_FIELD,
        //    PARAM_SET_REVERBERATION_DAMPING,
        //    PARAM_SET_REVERBERATION_WET_SIGNAL,
        //    PARAM_SET_REVERBERATION_DRY_SIGNAL,
        //    PARAM_SET_DIFFERENTIAL_SURROUND_ENABLE,
        //    PARAM_SET_DIFFERENTIAL_SURROUND_DELAY,
        //    PARAM_SET_FIELD_SURROUND_ENABLE,
        //    PARAM_SET_FIELD_SURROUND_DEPTH,
        //    PARAM_SET_FIELD_SURROUND_MID_IMAGE,
        //    PARAM_SET_IIR_EQUALIZER_ENABLE,
        //    PARAM_SET_IIR_EQUALIZER_BAND_LEVEL,
        //    PARAM_SET_SPECTRUM_EXTENSION_ENABLE,
        //    PARAM_SET_SPECTRUM_EXTENSION_STRENGTH,
        //    PARAM_SET_HEADPHONE_SURROUND_ENABLE,
        //    PARAM_SET_HEADPHONE_SURROUND_LEVEL,
        //} param_set_t;
        private const val PARAM_SET_RESET = 0u
        private const val PARAM_SET_DDC_ENABLE = 1u
        private const val PARAM_SET_DDC_COEFFICIENTS = 2u
        private const val PARAM_SET_VIPER_BASS_ENABLE = 3u
        private const val PARAM_SET_VIPER_BASS_MODE = 4u
        private const val PARAM_SET_VIPER_BASS_FREQUENCY = 5u
        private const val PARAM_SET_VIPER_BASS_GAIN = 6u
        private const val PARAM_SET_VIPER_CLARITY_ENABLE = 7u
        private const val PARAM_SET_VIPER_CLARITY_MODE = 8u
        private const val PARAM_SET_VIPER_CLARITY_GAIN = 9u
        private const val PARAM_SET_OUTPUT_GAIN = 10u
        private const val PARAM_SET_THRESHOLD_LIMIT = 11u
        private const val PARAM_SET_SPEAKER_OPTIMIZATION_ENABLE = 12u
        private const val PARAM_SET_ANALOGX_ENABLE = 13u
        private const val PARAM_SET_ANALOGX_LEVEL = 14u
        private const val PARAM_SET_TUBE_SIMULATOR_ENABLE = 15u
        private const val PARAM_SET_CURE_ENABLE = 16u
        private const val PARAM_SET_CURE_LEVEL = 17u
        private const val PARAM_SET_REVERBERATION_ENABLE = 18u
        private const val PARAM_SET_REVERBERATION_ROOM_SIZE = 19u
        private const val PARAM_SET_REVERBERATION_SOUND_FIELD = 20u
        private const val PARAM_SET_REVERBERATION_DAMPING = 21u
        private const val PARAM_SET_REVERBERATION_WET_SIGNAL = 22u
        private const val PARAM_SET_REVERBERATION_DRY_SIGNAL = 23u
        private const val PARAM_SET_DIFFERENTIAL_SURROUND_ENABLE = 24u
        private const val PARAM_SET_DIFFERENTIAL_SURROUND_DELAY = 25u
        private const val PARAM_SET_FIELD_SURROUND_ENABLE = 26u
        private const val PARAM_SET_FIELD_SURROUND_DEPTH = 27u
        private const val PARAM_SET_FIELD_SURROUND_MID_IMAGE = 28u
        private const val PARAM_SET_IIR_EQUALIZER_ENABLE = 29u
        private const val PARAM_SET_IIR_EQUALIZER_BAND_LEVEL = 30u
        private const val PARAM_SET_SPECTRUM_EXTENSION_ENABLE = 31u
        private const val PARAM_SET_SPECTRUM_EXTENSION_STRENGTH = 32u
        private const val PARAM_SET_HEADPHONE_SURROUND_ENABLE = 33u
        private const val PARAM_SET_HEADPHONE_SURROUND_LEVEL = 34u
    }
}