package com.aam.viper4android.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aam.viper4android.ViPERManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MasterLimiterViewModel @Inject constructor(
    private val viperManager: ViPERManager,
) : ViewModel() {
    private val _outputGain = MutableStateFlow(0)
    val outputGain = _outputGain.asStateFlow()

    private val _outputPan = MutableStateFlow(0)
    val outputPan = _outputPan.asStateFlow()

    private val _thresholdLimit = MutableStateFlow(0)
    val thresholdLimit = _thresholdLimit.asStateFlow()

    init {
        viewModelScope.launch {
            viperManager.currentPreset.collect { preset ->
                _outputGain.value = preset.masterLimiter.outputGain
                _outputPan.value = preset.masterLimiter.outputPan
                _thresholdLimit.value = preset.masterLimiter.thresholdLimit
            }
        }
    }

    fun setOutputGain(outputGain: Int) {
        _outputGain.value = outputGain
    }

    fun setOutputPan(outputPan: Int) {
        _outputPan.value = outputPan
    }

    fun setThresholdLimit(thresholdLimit: Int) {
        _thresholdLimit.value = thresholdLimit
    }
}