package com.aam.viper4android.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aam.viper4android.ViPERManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DynamicSystemViewModel @Inject constructor(
    private val viperManager: ViPERManager,
) : ViewModel() {
    private val _enabled = MutableStateFlow(false)
    val enabled = _enabled.asStateFlow()

    private val _deviceType = MutableStateFlow(0)
    val deviceType = _deviceType.asStateFlow()

    private val _dynamicBassStrength = MutableStateFlow(0)
    val dynamicBassStrength = _dynamicBassStrength.asStateFlow()

    init {
        viewModelScope.launch {
            viperManager.currentPreset.collect { preset ->
                _enabled.value = preset.dynamicSystem.enabled
                _deviceType.value = preset.dynamicSystem.deviceType
                _dynamicBassStrength.value = preset.dynamicSystem.dynamicBassStrength
            }
        }
    }

    fun setEnabled(enabled: Boolean) {
        _enabled.value = enabled
    }

    fun setDeviceType(deviceType: Int) {
        _deviceType.value = deviceType
    }

    fun setDynamicBassStrength(dynamicBassStrength: Int) {
        _dynamicBassStrength.value = dynamicBassStrength
    }
}