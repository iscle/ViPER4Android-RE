package com.aam.viper4android.ui.effect

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R
import com.aam.viper4android.ui.ValueSlider

class ViPERClarityState {
    var enabled by mutableStateOf(false)
    var clarityMode by mutableStateOf(0)
    var clarityGain by mutableStateOf(1)
}

@Composable
fun ViPERClarityEffect(state: ViPERClarityState) {
    EffectCard(icon = painterResource(R.drawable.ic_clarity), name = "ViPER clarity", enabled = state.enabled, onEnabledChange = { state.enabled = it }) {
        Column {
            ValueSlider(
                title = "Clarity gain",
                summaryUnit = "db",
                value = state.clarityGain,
                onValueChange = { state.clarityGain = it },
                valueRange = 0..9
            )
        }
    }
}