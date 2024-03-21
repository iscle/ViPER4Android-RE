package com.aam.viper4android.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aam.viper4android.ViPERManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ViPERBassViewModel @Inject constructor(
    private val viperManager: ViPERManager,
) : ViewModel() {
    val enabled = viperManager.viperBass.enabled
    val mode = viperManager.viperBass.mode
        .map { it.toInt() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = 0,
        )
    val frequency = viperManager.viperBass.frequency
        .map { it.toInt() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = 15,
        )
    val gain = viperManager.viperBass.gain
        .map { it.toInt() / 50 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = 1,
        )

    fun setEnabled(enabled: Boolean) {
        viperManager.viperBass.setEnabled(enabled)
    }
    fun setMode(mode: Int) {
        viperManager.viperBass.setMode(mode.toUByte())
    }
    fun setFrequency(frequency: Int) {
        viperManager.viperBass.setFrequency(frequency.toUByte())
    }
    fun setGain(gain: Int) {
        viperManager.viperBass.setGain((gain * 50).toUShort())
    }
}