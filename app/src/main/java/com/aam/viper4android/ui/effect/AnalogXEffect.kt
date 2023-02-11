package com.aam.viper4android.ui.effect

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R
import com.aam.viper4android.ui.ValueSlider

@Composable
fun AnalogXEffect() {
    var enabled by remember { mutableStateOf(false) }
    EffectCard(icon = painterResource(R.drawable.ic_analogx), name = "AnalogX", enabled = enabled, onEnabledChange = { enabled = it }) {
        var level by rememberSaveable { mutableStateOf(0) } // TODO: Move to state
        Column {
            ValueSlider(
                title = "Level",
                summary = (level + 1).toString(),
                value = level,
                onValueChange = { level = it },
                valueRange = 0..2
            )
        }
    }
}