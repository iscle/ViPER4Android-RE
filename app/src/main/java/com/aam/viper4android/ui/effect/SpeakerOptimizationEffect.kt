package com.aam.viper4android.ui.effect

import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R

class SpeakerOptimizationState {
    var enabled by mutableStateOf(false)
}

@Composable
fun SpeakerOptimizationEffect(state: SpeakerOptimizationState) {
    EffectCard(icon = painterResource(R.drawable.ic_speaker), name = "Speaker optimization", enabled = state.enabled, onEnabledChange = { state.enabled = it })

}