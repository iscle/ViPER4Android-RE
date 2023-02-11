package com.aam.viper4android.ui.effect

import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R

@Composable
fun FIREqualizerEffect() {
    var enabled by remember { mutableStateOf(false) }
    EffectCard(
        icon = painterResource(R.drawable.ic_equalizer),
        name = "FIR equalizer",
        enabled = enabled,
        onEnabledChange = { enabled = it }) {

    }
}