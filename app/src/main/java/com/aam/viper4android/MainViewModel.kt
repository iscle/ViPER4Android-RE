package com.aam.viper4android

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(EffectUiState())
    val uiState: StateFlow<EffectUiState> = _uiState.asStateFlow()

    init {
        resetEffect()
    }

    fun resetEffect() {
        _uiState.value = EffectUiState(enabled = false)
    }
}