package com.aam.viper4android.ui.effect

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R
import com.aam.viper4android.ui.ValueSlider

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
            // TODO: Device type picker
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