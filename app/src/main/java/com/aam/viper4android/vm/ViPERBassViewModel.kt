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
class ViPERBassViewModel @Inject constructor(
    private val viperManager: ViPERManager,
) : ViewModel() {
    private val _enabled = MutableStateFlow(false)
    val enabled = _enabled.asStateFlow()

    private val _bassMode = MutableStateFlow(0)
    val bassMode = _bassMode.asStateFlow()

    private val _bassFrequency = MutableStateFlow(0)
    val bassFrequency = _bassFrequency.asStateFlow()

    private val _bassGain = MutableStateFlow(0)
    val bassGain = _bassGain.asStateFlow()

    init {
        viewModelScope.launch {
            viperManager.currentPreset.collect { preset ->
                _enabled.value = preset.viperBass.enabled
                _bassMode.value = preset.viperBass.bassMode
                _bassFrequency.value = preset.viperBass.bassFrequency
                _bassGain.value = preset.viperBass.bassGain
            }
        }
    }

    fun setEnabled(enabled: Boolean) {
        _enabled.value = enabled
    }

    fun setBassMode(mode: Int) {
        _bassMode.value = mode
    }

    fun setBassFrequency(frequency: Int) {
        _bassFrequency.value = frequency
    }

    fun setBassGain(gain: Int) {
        _bassGain.value = gain
    }
}