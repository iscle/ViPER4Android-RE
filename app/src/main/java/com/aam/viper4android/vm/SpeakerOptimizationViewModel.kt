package com.aam.viper4android.vm

import androidx.lifecycle.ViewModel
import com.aam.viper4android.ViPERManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SpeakerOptimizationViewModel @Inject constructor(
    private val viperManager: ViPERManager,
) : ViewModel() {
    val enabled = viperManager.speakerOptimization.enabled

    fun setEnabled(enabled: Boolean) {
        viperManager.speakerOptimization.setEnabled(enabled)
    }
}