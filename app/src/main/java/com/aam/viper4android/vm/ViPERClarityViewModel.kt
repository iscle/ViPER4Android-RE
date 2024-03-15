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
class ViPERClarityViewModel @Inject constructor(
    private val viperManager: ViPERManager,
) : ViewModel() {
    private val _enabled = MutableStateFlow(false)
    val enabled = _enabled.asStateFlow()

    private val _clarityMode = MutableStateFlow(0)
    val clarityMode = _clarityMode.asStateFlow()

    private val _clarityGain = MutableStateFlow(0)
    val clarityGain = _clarityGain.asStateFlow()

    init {
        viewModelScope.launch {
            viperManager.currentPreset.collect { preset ->
                _enabled.value = preset.viperClarity.enabled
                _clarityMode.value = preset.viperClarity.clarityMode
                _clarityGain.value = preset.viperClarity.clarityGain
            }
        }
    }

    fun setEnabled(enabled: Boolean) {
        _enabled.value = enabled
    }

    fun setClarityMode(mode: Int) {
        _clarityMode.value = mode
    }

    fun setClarityGain(gain: Int) {
        _clarityGain.value = gain
    }
}