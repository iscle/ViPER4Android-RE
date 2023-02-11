package com.aam.viper4android.ui.effect

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R
import com.aam.viper4android.ui.ValueSlider

@Composable
fun FieldSurroundEffect() {
    var enabled by remember { mutableStateOf(false) }
    EffectCard(
        icon = painterResource(R.drawable.ic_surround),
        name = "Field surround",
        enabled = enabled,
        onEnabledChange = { enabled = it }) {
        var surroundStrength by rememberSaveable { mutableStateOf(0) } // TODO: Move to state
        var midImageStrength by rememberSaveable { mutableStateOf(5) } // TODO: Move to state
        Column {
            ValueSlider(
                title = "Surround strength",
                summary = (surroundStrength + 1).toString(),
                value = surroundStrength,
                onValueChange = { surroundStrength = it },
                valueRange = 0..8
            )
            ValueSlider(
                title = "Mid image strength",
                summary = (midImageStrength + 1).toString(),
                value = midImageStrength,
                onValueChange = { midImageStrength = it },
                valueRange = 0..10
            )
        }
    }
}