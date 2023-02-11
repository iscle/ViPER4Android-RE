package com.aam.viper4android.ui.effect

import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R

@Composable
fun PlaybackGainControlEffect() {
    var enabled by remember { mutableStateOf(false) }
    EffectCard(icon = painterResource(R.drawable.ic_playback_control), name = "Playback gain control", enabled = enabled, onEnabledChange = { enabled = it })

}