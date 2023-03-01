package com.aam.viper4android.ui.effect

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R
import com.aam.viper4android.ui.ValueSlider
import com.aam.viper4android.ui.component.ValuePicker

@Composable
fun DynamicSystemEffect() {
    var enabled by remember { mutableStateOf(false) }
    EffectCard(icon = painterResource(R.drawable.ic_dynamic_system),
        name = "Dynamic system",
        enabled = enabled,
        onEnabledChange = { enabled = it }) {
        var deviceType by rememberSaveable { mutableStateOf(0) } // TODO: Move to state
        var dynamicBassStrength by rememberSaveable { mutableStateOf(0) } // TODO: Move to state
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
            ), selectedIndex = deviceType, onSelectedIndexChange = { deviceType = it })
            ValueSlider(
                title = "Dynamic bass strength",
                summaryUnit = "%",
                value = dynamicBassStrength,
                onValueChange = { dynamicBassStrength = it },
                valueRange = 0..100
            )
        }
    }
}