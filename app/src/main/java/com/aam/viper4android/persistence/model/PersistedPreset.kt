package com.aam.viper4android.persistence.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "presets")
data class PersistedPreset(
    @PrimaryKey @ColumnInfo(name = "device_id") val deviceId: String,
    @Embedded(prefix = "analog_x") val analogX: AnalogX,
    @Embedded(prefix = "auditory_system_protection") val auditorySystemProtection: AuditorySystemProtection,
    @Embedded(prefix = "convolver") val convolver: Convolver,
    @Embedded(prefix = "differential_surround") val differentialSurround: DifferentialSurround,
    @Embedded(prefix = "dynamic_system") val dynamicSystem: DynamicSystem,
    @Embedded(prefix = "fet_compressor") val fetCompressor: FETCompressor,
    @Embedded(prefix = "field_surround_effect") val fieldSurroundEffect: FieldSurroundEffect,
    @Embedded(prefix = "fir_equalizer") val firequalizer: FIREqualizer,
    @Embedded(prefix = "headphone_surround_plus") val headphoneSurroundPlus: HeadphoneSurroundPlus,
    @Embedded(prefix = "master_limiter") val masterLimiter: MasterLimiter,
    @Embedded(prefix = "playback_gain_control") val playbackGainControl: PlaybackGainControl,
    @Embedded(prefix = "reverberation") val reverberation: Reverberation,
    @Embedded(prefix = "speaker_optimization") val speakerOptimization: SpeakerOptimization,
    @Embedded(prefix = "spectrum_extension") val spectrumExtension: SpectrumExtension,
    @Embedded(prefix = "tube_simulator_6n1j") val tubeSimulator6N1J: TubeSimulator6N1J,
    @Embedded(prefix = "viper_bass") val viperBass: ViPERBass,
    @Embedded(prefix = "viper_clarity") val viperClarity: ViPERClarity,
    @Embedded(prefix = "viper_ddc") val viperDDC: ViPERDDC,
) {
    data class AnalogX(
        val field: String,
    )

    data class AuditorySystemProtection(
        val field: String,
    )

    data class Convolver(
        val field: String,
    )

    data class DifferentialSurround(
        val field: String,
    )

    data class DynamicSystem(
        val field: String,
    )

    data class FETCompressor(
        val field: String,
    )

    data class FieldSurroundEffect(
        val field: String,
    )

    data class FIREqualizer(
        val field: String,
    )

    data class HeadphoneSurroundPlus(
        val field: String,
    )

    data class MasterLimiter(
        val field: String,
    )

    data class PlaybackGainControl(
        val field: String,
    )

    data class Reverberation(
        val field: String,
    )

    data class SpeakerOptimization(
        val field: String,
    )

    data class SpectrumExtension(
        val field: String,
    )

    data class TubeSimulator6N1J(
        val field: String,
    )

    data class ViPERBass(
        val field: String,
    )

    data class ViPERClarity(
        val field: String,
    )

    data class ViPERDDC(
        val field: String,
    )
}
