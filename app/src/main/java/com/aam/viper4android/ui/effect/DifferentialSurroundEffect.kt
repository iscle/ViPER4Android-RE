package com.aam.viper4android.ui.effect

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R
import com.aam.viper4android.ui.ValueSlider

@Composable
fun DifferentialSurroundEffect() {
    var enabled by remember { mutableStateOf(false) }
    EffectCard(
        icon = painterResource(R.drawable.ic_surround),
        name = "Differential surround",
        enabled = enabled,
        onEnabledChange = { enabled = it }) {
        var delay by rememberSaveable { mutableStateOf(4) } // TODO: Move to state
        Column {
            ValueSlider(
                title = "Delay",
                summary = (delay + 1).toString(),
                summaryUnit = "ms",
                value = delay,
                onValueChange = { delay = it },
                valueRange = 0..19
            )
        }
    }
}