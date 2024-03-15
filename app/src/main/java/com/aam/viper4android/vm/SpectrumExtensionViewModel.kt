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
class SpectrumExtensionViewModel @Inject constructor(
    private val viperManager: ViPERManager,
) : ViewModel() {
    private val _enabled = MutableStateFlow(false)
    val enabled = _enabled.asStateFlow()

    private val _strength = MutableStateFlow(0)
    val strength = _strength.asStateFlow()

    init {
        viewModelScope.launch {
            viperManager.currentPreset.collect { preset ->
                _enabled.value = preset.spectrumExtension.enabled
                _strength.value = preset.spectrumExtension.strength
            }
        }
    }

    fun setEnabled(enabled: Boolean) {
        _enabled.value = enabled
    }

    fun setStrength(strength: Int) {
        _strength.value = strength
    }
}