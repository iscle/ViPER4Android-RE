package com.aam.viper4android.ui.effect

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R
import com.aam.viper4android.ui.ValueSlider
import com.aam.viper4android.ui.component.ValuePicker

class DynamicSystemState {
    var enabled by mutableStateOf(false)
    var deviceType by mutableStateOf(0)
    var dynamicBassStrength by mutableStateOf(0)
}

@Composable
fun DynamicSystemEffect(state: DynamicSystemState) {
    EffectCard(icon = painterResource(R.drawable.ic_dynamic_system),
        name = "Dynamic system",
        enabled = state.enabled,
        onEnabledChange = { state.enabled = it }) {
        Column {
            ValuePicker(title = "Device type", values = arrayOf(
                "Extreme headphone(v2)",
                "High-end headphone(v2)",
                "Common headphone(v2)",
                "Low-end headphone(v2)",
                "Common earphone(v2)",
                "Extreme headphone(v1)",
                "High-end headphone(v1)",
                "Common headphone(v1)",
                "Common earphone(v1)"
            ), selectedIndex = state.deviceType, onSelectedIndexChange = { state.deviceType = it })
            ValueSlider(
                title = "Dynamic bass strength",
                summaryUnit = "%",
                value = state.dynamicBassStrength,
                onValueChange = { state.dynamicBassStrength = it },
                valueRange = 0..100
            )
        }
    }
}