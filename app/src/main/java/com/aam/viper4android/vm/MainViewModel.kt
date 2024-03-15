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
    private val _enabled = MutableStateFlow(false)
    val enabled: StateFlow<Boolean> = _enabled

    init {
        viewModelScope.launch {
            viperManager.currentPreset.collect { preset ->
                _enabled.value = preset.enabled
            }
        }
    }

    fun setEnabled(enabled: Boolean) {
        _enabled.value = enabled
    }
}