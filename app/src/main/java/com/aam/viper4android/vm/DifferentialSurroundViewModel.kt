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
class DifferentialSurroundViewModel @Inject constructor(
    private val viperManager: ViPERManager,
) : ViewModel() {
    val enabled = viperManager.differentialSurround.enabled
    val delay = viperManager.differentialSurround.delay
        .map { it.toInt() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = 0,
        )

    fun setEnabled(enabled: Boolean) {
        viperManager.differentialSurround.setEnabled(enabled)
    }

    fun setDelay(delay: Int) {
        viperManager.differentialSurround.setDelay(delay.toUShort())
    }
}