package com.aam.viper4android.vm

import androidx.lifecycle.ViewModel
import com.aam.viper4android.ViPERManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AnalogXViewModel @Inject constructor(
    private val viperManager: ViPERManager,
) : ViewModel() {
    val enabled = viperManager.analogX.enabled
    val level = viperManager.analogX.level

    fun setEnabled(enabled: Boolean) {
        viperManager.analogX.setEnabled(enabled)
    }
    fun setLevel(level: Int) {
        viperManager.analogX.setLevel(level)
    }
}