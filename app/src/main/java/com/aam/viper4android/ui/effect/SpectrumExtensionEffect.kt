package com.aam.viper4android.ui.effect

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R
import com.aam.viper4android.ui.ValueSlider

class SpectrumExtensionState {
    var enabled by mutableStateOf(false)
    var strength by mutableStateOf(10)
}

@Composable
fun SpectrumExtensionEffect(state: SpectrumExtensionState) {
    EffectCard(
        icon = painterResource(R.drawable.ic_vse),
        name = "Spectrum extension",
        enabled = state.enabled,
        onEnabledChange = { state.enabled = it }) {
        Column {
            ValueSlider(
                title = "Strength",
                summaryUnit = "%",
                value = state.strength,
                onValueChange = { state.strength = it },
                valueRange = 0..100
            )
        }
    }
}