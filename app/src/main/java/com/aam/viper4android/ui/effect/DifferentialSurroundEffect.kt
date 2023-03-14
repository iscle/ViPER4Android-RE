package com.aam.viper4android.ui.effect

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R
import com.aam.viper4android.ui.ValueSlider

class DifferentialSurroundState {
    var enabled by mutableStateOf(false)
    var delay by mutableStateOf(4)
}

@Composable
fun DifferentialSurroundEffect(state: DifferentialSurroundState) {
    EffectCard(
        icon = painterResource(R.drawable.ic_surround),
        name = "Differential surround",
        enabled = state.enabled,
        onEnabledChange = { state.enabled = it }) {
        Column {
            ValueSlider(
                title = "Delay",
                summary = (state.delay + 1).toString(),
                summaryUnit = "ms",
                value = state.delay,
                onValueChange = { state.delay = it },
                valueRange = 0..19
            )
        }
    }
}