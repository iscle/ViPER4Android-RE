package com.aam.viper4android.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aam.viper4android.ViPERManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReverberationViewModel @Inject constructor(
    private val viperManager: ViPERManager,
) : ViewModel() {
    val enabled = viperManager.reverberation.enabled
    val roomSize = viperManager.reverberation.roomSize
        .map { it.toInt() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = 0,
        )
    val soundField = viperManager.reverberation.soundField
        .map { it.toInt() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = 0,
        )
    val damping = viperManager.reverberation.damping
        .map { it.toInt() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = 0,
        )
    val wetSignal = viperManager.reverberation.wetSignal
        .map { it.toInt() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = 0,
        )
    val drySignal = viperManager.reverberation.drySignal
        .map { it.toInt() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = 50,
        )

    fun setEnabled(enabled: Boolean) {
        viperManager.reverberation.setEnabled(enabled)
    }

    fun setRoomSize(roomSize: Int) {
        viperManager.reverberation.setRoomSize(roomSize.toUByte())
    }

    fun setSoundField(soundField: Int) {
        viperManager.reverberation.setSoundField(soundField.toUByte())
    }

    fun setDamping(damping: Int) {
        viperManager.reverberation.setDamping(damping.toUByte())
    }

    fun setWetSignal(wetSignal: Int) {
        viperManager.reverberation.setWetSignal(wetSignal.toUByte())
    }

    fun setDrySignal(drySignal: Int) {
        viperManager.reverberation.setDrySignal(drySignal.toUByte())
    }
}