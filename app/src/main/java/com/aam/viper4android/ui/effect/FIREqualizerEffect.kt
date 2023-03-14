package com.aam.viper4android.ui.effect

import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R

class FIREqualizerState {
    var enabled by mutableStateOf(false)
}

@Composable
fun FIREqualizerEffect(state: FIREqualizerState) {
    EffectCard(
        icon = painterResource(R.drawable.ic_equalizer),
        name = "FIR equalizer",
        enabled = state.enabled,
        onEnabledChange = { state.enabled = it }) {

    }
}