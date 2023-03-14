package com.aam.viper4android.ui.effect

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R
import com.aam.viper4android.ui.ValueSlider

class FieldSurroundState {
    var enabled by mutableStateOf(false)
    var surroundStrength by mutableStateOf(0)
    var midImageStrength by mutableStateOf(5)
}

@Composable
fun FieldSurroundEffect(state: FieldSurroundState) {
    EffectCard(
        icon = painterResource(R.drawable.ic_surround),
        name = "Field surround",
        enabled = state.enabled,
        onEnabledChange = { state.enabled = it }) {
        Column {
            ValueSlider(
                title = "Surround strength",
                summary = (state.surroundStrength + 1).toString(),
                value = state.surroundStrength,
                onValueChange = { state.surroundStrength = it },
                valueRange = 0..8
            )
            ValueSlider(
                title = "Mid image strength",
                summary = (state.midImageStrength + 1).toString(),
                value = state.midImageStrength,
                onValueChange = { state.midImageStrength = it },
                valueRange = 0..10
            )
        }
    }
}