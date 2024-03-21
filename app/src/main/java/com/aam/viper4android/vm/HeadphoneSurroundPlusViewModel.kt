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
class HeadphoneSurroundPlusViewModel @Inject constructor(
    private val viperManager: ViPERManager,
) : ViewModel() {
    val enabled = viperManager.headphoneSurroundPlus.enabled
    val level = viperManager.headphoneSurroundPlus.level
        .map { it.toInt() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = 0,
        )

    fun setEnabled(enabled: Boolean) {
        viperManager.headphoneSurroundPlus.setEnabled(enabled)
    }

    fun setLevel(level: Int) {
        viperManager.headphoneSurroundPlus.setLevel(level.toUByte())
    }
}