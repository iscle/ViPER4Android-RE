package com.aam.viper4android

data class Preset(
    var enabled: Boolean = false,
    val analogX: AnalogX = AnalogX(),
    val auditorySystemProtection: AuditorySystemProtection = AuditorySystemProtection(),
    val convolver: Convolver = Convolver(),
    val differentialSurround: DifferentialSurround = DifferentialSurround(),
    val dynamicSystem: DynamicSystem = DynamicSystem(),
    val fetCompressor: FETCompressor = FETCompressor(),
    val fieldSurround: FieldSurroundEffect = FieldSurroundEffect(),
    val firEqualizer: FIREqualizer = FIREqualizer(),
    val headphoneSurroundPlus: HeadphoneSurroundPlus = HeadphoneSurroundPlus(),
    val masterLimiter: MasterLimiter = MasterLimiter(),
    val playbackGainControl: PlaybackGainControl = PlaybackGainControl(),
    val reverberation: Reverberation = Reverberation(),
    val speakerOptimization: SpeakerOptimization = SpeakerOptimization(),
    val spectrumExtension: SpectrumExtension = SpectrumExtension(),
    val tubeSimulator6N1J: TubeSimulator6N1J = TubeSimulator6N1J(),
    val viperBass: ViPERBass = ViPERBass(),
    val viperClarity: ViPERClarity = ViPERClarity(),
    val viperDdc: ViPERDDC = ViPERDDC(),
) {
    data class AnalogX(
        var enabled: Boolean = false,
        var level: Int = 0,
    )

    data class AuditorySystemProtection(
        var enabled: Boolean = false,
    )

    data class Convolver(
        var enabled: Boolean = false,
    )

    data class DifferentialSurround(
        var enabled: Boolean = false,
        var delay: Int = 4,
    )

    data class DynamicSystem(
        var enabled: Boolean = false,
        var deviceType: Int = 0,
        var dynamicBassStrength: Int = 0,
    )

    data class FETCompressor(
        var enabled: Boolean = false,
    )

    data class FieldSurroundEffect(
        var enabled: Boolean = false,
        var surroundStrength: Int = 0,
        var midImageStrength: Int = 5,
    )

    data class FIREqualizer(
        var enabled: Boolean = false,
    )

    data class HeadphoneSurroundPlus(
        var enabled: Boolean = false,
    )

    data class MasterLimiter(
        var outputGain: Int = 11,
        var outputPan: Int = 50,
        var thresholdLimit: Int = 5,
    )

    data class PlaybackGainControl(
        var enabled: Boolean = false,
    )

    data class Reverberation(
        var enabled: Boolean = false,
    )

    data class SpeakerOptimization(
        var enabled: Boolean = false,
    )

    data class SpectrumExtension(
        var enabled: Boolean = false,
        var strength: Int = 10,
    )

    data class TubeSimulator6N1J(
        var enabled: Boolean = false,
    )

    data class ViPERBass(
        var enabled: Boolean = false,
        var bassMode: Int = 0,
        var bassFrequency: Int = 55,
        var bassGain: Int = 0,
    )

    data class ViPERClarity(
        var enabled: Boolean = false,
        var clarityMode: Int = 0,
        var clarityGain: Int = 1,
    )

    data class ViPERDDC(
        var enabled: Boolean = false,
    )
}