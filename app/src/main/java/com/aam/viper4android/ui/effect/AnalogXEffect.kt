package com.aam.viper4android.ui.effect

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R
import com.aam.viper4android.ui.ValueSlider

class AnalogXState {
    var enabled by mutableStateOf(false)
    var level by mutableStateOf(0)
}

@Composable
fun AnalogXEffect(state: AnalogXState) {
    EffectCard(icon = painterResource(R.drawable.ic_analogx), name = "AnalogX", enabled = state.enabled, onEnabledChange = { state.enabled = it }) {
        Column {
            ValueSlider(
                title = "Level",
                summary = (state.level + 1).toString(),
                value = state.level,
                onValueChange = { state.level = it },
                valueRange = 0..2
            )
        }
    }
}