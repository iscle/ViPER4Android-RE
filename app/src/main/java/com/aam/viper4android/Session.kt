package com.aam.viper4android

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

data class Session(
    private val viperManager: ViPERManager,
    val packageName: String,
    val sessionId: Int,
) {
    val effect = ViPEREffect(sessionId)
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        if (!effect.audioEffect.hasControl()) {
            release()
            throw IllegalStateException("Failed to get control of audio effect")
        }

        collectEffects()
    }

    private fun collectEffects() {
        scope.launch {
            viperManager.enabled.collect {
                effect.audioEffect.enabled = it
            }
        }
        scope.launch {
            viperManager.analogX.enabled.collect {
                effect.analogX.setEnabled(it)
            }
        }
        scope.launch {
            viperManager.analogX.level.collect {
//                effect.analogX.level = it
            }
        }
        scope.launch {
            viperManager.auditorySystemProtection.enabled.collect {
                effect.auditorySystemProtection.setEnabled(it)
            }
        }
        scope.launch {
            viperManager.convolver.enabled.collect {
                effect.convolver.enabled = it
            }
        }
        scope.launch {
            viperManager.differentialSurround.enabled.collect {
                effect.differentialSurround.setEnabled(it)
            }
        }
        scope.launch {
            viperManager.differentialSurround.delay.collect {
                effect.differentialSurround.setDelay(it)
            }
        }
        scope.launch {
            viperManager.dynamicSystem.enabled.collect {
                effect.dynamicSystem.enabled = it
            }
        }
        scope.launch {
            viperManager.dynamicSystem.deviceType.collect {
//                effect.dynamicSystem.deviceType = it
            }
        }
        scope.launch {
            viperManager.dynamicSystem.dynamicBassStrength.collect {
//                effect.dynamicSystem.dynamicBassStrength = it
            }
        }
        scope.launch {
            viperManager.fetCompressor.enabled.collect {
                effect.fetCompressor.enabled = it
            }
        }
        scope.launch {
            viperManager.fieldSurroundEffect.enabled.collect {
//                effect.fieldSurroundEffect.enabled = it
            }
        }
        scope.launch {
            viperManager.fieldSurroundEffect.surroundStrength.collect {
//                effect.fieldSurroundEffect.surroundStrength = it
            }
        }
        scope.launch {
            viperManager.fieldSurroundEffect.midImageStrength.collect {
//                effect.fieldSurroundEffect.midImageStrength = it
            }
        }
        scope.launch {
            viperManager.firEqualizer.enabled.collect {
                effect.iirEqualizer.setEnabled(it)
            }
        }
        scope.launch {
            viperManager.headphoneSurroundPlus.enabled.collect {
                effect.headphoneSurroundPlus.setEnabled(it)
            }
        }
        scope.launch {
            viperManager.headphoneSurroundPlus.level.collect {
                effect.headphoneSurroundPlus.setLevel(it)
            }
        }
        scope.launch {
            // collect outputGain and outputPan together to perform operations on them
            viperManager.masterLimiter.outputGain.combine(viperManager.masterLimiter.outputPan) { gain, pan ->
                    gain to pan
                }.collect { (gain, pan) ->
                    val panL = if (pan < 50u) 1.0f else ((100.0f - pan.toFloat()) / 50.0f)
                    val panR = if (pan > 50u) 1.0f else (pan.toFloat() / 50.0f)
                    val gainL = (gain.toFloat() * panL).roundToInt().toUByte()
                    val gainR = (gain.toFloat() * panR).roundToInt().toUByte()
                    effect.masterLimiter.setOutputGain(gainL, gainR)
                }
        }
        scope.launch {
            viperManager.masterLimiter.thresholdLimit.collect {
                effect.masterLimiter.setThresholdLimit(it)
            }
        }
        scope.launch {
            viperManager.playbackGainControl.enabled.collect {
                effect.playbackGainControl.enabled = it
            }
        }
        scope.launch {
            viperManager.reverberation.enabled.collect {
                effect.reverberation.setEnabled(it)
            }
        }
        scope.launch {
            viperManager.reverberation.roomSize.collect {
                effect.reverberation.setRoomSize(it)
            }
        }
        scope.launch {
            viperManager.reverberation.soundField.collect {
                effect.reverberation.setSoundField(it)
            }
        }
        scope.launch {
            viperManager.reverberation.damping.collect {
                effect.reverberation.setDamping(it)
            }
        }
        scope.launch {
            viperManager.reverberation.wetSignal.collect {
                effect.reverberation.setWetSignal(it)
            }
        }
        scope.launch {
            viperManager.reverberation.drySignal.collect {
                effect.reverberation.setDrySignal(it)
            }
        }
        scope.launch {
            viperManager.speakerOptimization.enabled.collect {
                effect.speakerOptimization.setEnabled(it)
            }
        }
        scope.launch {
            viperManager.spectrumExtension.enabled.collect {
                effect.spectrumExtension.setEnabled(it)
            }
        }
        scope.launch {
            viperManager.spectrumExtension.strength.collect {
                effect.spectrumExtension.setStrength(it)
            }
        }
        scope.launch {
            viperManager.tubeSimulator6N1J.enabled.collect {
                effect.tubeSimulator.setEnabled(it)
            }
        }
        scope.launch {
            viperManager.viperBass.enabled.collect {
                effect.viperBass.setEnabled(it)
            }
        }
        scope.launch {
            viperManager.viperBass.mode.collect {
                effect.viperBass.setMode(it)
            }
        }
        scope.launch {
            viperManager.viperBass.frequency.collect {
                effect.viperBass.setFrequency(it)
            }
        }
        scope.launch {
            viperManager.viperBass.gain.collect {
                effect.viperBass.setGain(it)
            }
        }
        scope.launch {
            viperManager.viperClarity.enabled.collect {
                effect.viperClarity.setEnabled(it)
            }
        }
        scope.launch {
            viperManager.viperClarity.mode.collect {
                effect.viperClarity.setMode(it)
            }
        }
        scope.launch {
            viperManager.viperClarity.gain.collect {
                effect.viperClarity.setGain(it)
            }
        }
        scope.launch {
            viperManager.viperDdc.enabled.collect {
//                effect.viperDdc.enabled = it
            }
        }
    }

    fun release() {
        scope.cancel()
        effect.audioEffect.release()
    }
}