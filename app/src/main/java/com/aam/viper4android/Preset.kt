package com.aam.viper4android

data class Preset(
    var name: String = "Untitled preset",
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
        var delay: UShort = 4u,
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
        var level: UByte = 0u,
    )

    data class MasterLimiter(
        var outputGain: UByte = 100u,
        var outputPan: UByte = 50u,
        var thresholdLimit: UByte = 100u,
    )

    data class PlaybackGainControl(
        var enabled: Boolean = false,
    )

    data class Reverberation(
        var enabled: Boolean = false,
        var roomSize: UByte = 0u,
        var soundField: UByte = 0u,
        var damping: UByte = 0u,
        var wetSignal: UByte = 0u,
        var drySignal: UByte = 50u,
    )

    data class SpeakerOptimization(
        var enabled: Boolean = false,
    )

    data class SpectrumExtension(
        var enabled: Boolean = false,
        var strength: UByte = 10u,
    )

    data class TubeSimulator6N1J(
        var enabled: Boolean = false,
    )

    data class ViPERBass(
        var enabled: Boolean = false,
        var mode: UByte = 0u,
        var frequency: UByte = 15u,
        var gain: UShort = 50u,
    )

    data class ViPERClarity(
        var enabled: Boolean = false,
        var mode: UByte = 0u,
        var gain: UShort = 1u,
    )

    data class ViPERDDC(
        var enabled: Boolean = false,
    )
}