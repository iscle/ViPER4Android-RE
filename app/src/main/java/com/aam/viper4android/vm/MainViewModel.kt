package com.aam.viper4android.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aam.viper4android.ViPERManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val viperManager: ViPERManager,
) : ViewModel() {
    private val _presetName = MutableStateFlow("")
    val presetName: StateFlow<String> = _presetName

    val enabled = viperManager.enabled

    init {
        viewModelScope.launch {
            viperManager.currentPreset.collect { preset ->
                _presetName.value = preset.name
            }
        }
    }

    fun setPresetName(name: String) {
        // TODO
    }

    fun setEnabled(enabled: Boolean) = viperManager.setEnabled(enabled)
}