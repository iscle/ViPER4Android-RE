package com.aam.viper4android.ui.effect

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R
import com.aam.viper4android.ui.ValueSlider

@Composable
fun ViPERClarityEffect() {
    var enabled by remember { mutableStateOf(false) }
    EffectCard(icon = painterResource(R.drawable.ic_clarity), name = "ViPER clarity", enabled = enabled, onEnabledChange = { enabled = it }) {
        var clarityGain by rememberSaveable { mutableStateOf(1) } // TODO: Move to state
        Column {
            ValueSlider(
                title = "Clarity gain",
                summaryUnit = "db",
                value = clarityGain,
                onValueChange = { clarityGain = it },
                valueRange = 0..9
            )
        }
    }
}