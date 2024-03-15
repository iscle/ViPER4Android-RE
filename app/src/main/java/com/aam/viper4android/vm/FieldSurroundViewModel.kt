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
class FieldSurroundViewModel @Inject constructor(
    private val viperManager: ViPERManager,
) : ViewModel() {
    private val _enabled = MutableStateFlow(false)
    val enabled = _enabled.asStateFlow()

    private val _surroundStrength = MutableStateFlow(0)
    val surroundStrength = _surroundStrength.asStateFlow()

    private val _midImageStrength = MutableStateFlow(5)
    val midImageStrength = _midImageStrength.asStateFlow()

    init {
        viewModelScope.launch {
            viperManager.currentPreset.collect { preset ->
                _enabled.value = preset.fieldSurround.enabled
                _surroundStrength.value = preset.fieldSurround.surroundStrength
                _midImageStrength.value = preset.fieldSurround.midImageStrength
            }
        }
    }

    fun setEnabled(enabled: Boolean) {
        _enabled.value = enabled
    }

    fun setSurroundStrength(surroundStrength: Int) {
        _surroundStrength.value = surroundStrength
    }

    fun setMidImageStrength(midImageStrength: Int) {
        _midImageStrength.value = midImageStrength
    }
}