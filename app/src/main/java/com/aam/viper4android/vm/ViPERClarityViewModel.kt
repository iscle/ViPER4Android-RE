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
class ViPERClarityViewModel @Inject constructor(
    private val viperManager: ViPERManager,
) : ViewModel() {
    val enabled = viperManager.viperClarity.enabled
    val mode = viperManager.viperClarity.mode
        .map { it.toInt() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = 0
        )
    val gain = viperManager.viperClarity.gain.map {
        it.toInt() / 50
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = 0
    )

    fun setEnabled(enabled: Boolean) {
        viperManager.viperClarity.setEnabled(enabled)
    }
    fun setMode(mode: Int) {
        viperManager.viperClarity.setMode(mode.toUByte())
    }
    fun setGain(gain: Int) {
        viperManager.viperClarity.setGain((gain * 50).toUShort())
    }
}