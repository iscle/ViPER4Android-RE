package com.aam.viper4android.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aam.viper4android.ViPERManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private val outputGainValues = arrayOf<UByte>(
    1u,
    5u,
    10u,
    20u,
    30u,
    40u,
    50u,
    60u,
    70u,
    80u,
    90u,
    100u,
    110u,
    120u,
    130u,
    140u,
    150u,
    160u,
    170u,
    180u,
    190u,
    200u,
)

private val thresholdLimitValues = arrayOf<UByte>(
    30u,
    50u,
    70u,
    80u,
    90u,
    100u,
)

@HiltViewModel
class MasterLimiterViewModel @Inject constructor(
    private val viperManager: ViPERManager,
) : ViewModel() {
    val outputGain = viperManager.masterLimiter.outputGain
        .map(outputGainValues::indexOf)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = outputGainValues.indexOf(100u)
        )
    val outputPan = viperManager.masterLimiter.outputPan
        .map {
            it.toInt()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = 50
        )
    val thresholdLimit = viperManager.masterLimiter.thresholdLimit
        .map(thresholdLimitValues::indexOf)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = thresholdLimitValues.indexOf(100u)
        )

    fun setOutputGain(outputGain: Int) {
        viperManager.masterLimiter.setOutputGain(outputGainValues[outputGain])
    }

    fun setOutputPan(outputPan: Int) {
        viperManager.masterLimiter.setOutputPan(outputPan.toUByte())
    }

    fun setThresholdLimit(thresholdLimit: Int) {
        viperManager.masterLimiter.setThresholdLimit(thresholdLimitValues[thresholdLimit])
    }
}