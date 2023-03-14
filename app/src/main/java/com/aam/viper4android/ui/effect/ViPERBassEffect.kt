package com.aam.viper4android.ui.effect

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R
import com.aam.viper4android.ui.ValueSlider
import com.aam.viper4android.ui.component.ValuePicker

class ViPERBassState {
    var enabled by mutableStateOf(false)
    var bassMode by mutableStateOf(0)
    var bassFrequency by mutableStateOf(55)
    var bassGain by mutableStateOf(0)
}

@Composable
fun ViPERBassEffect(state: ViPERBassState) {
    EffectCard(icon = painterResource(R.drawable.ic_bass), name = "ViPER bass", enabled = state.enabled, onEnabledChange = { state.enabled = it }) {
        Column {
            ValuePicker(
                title = "Bass mode",
                values = arrayOf("Natural bass", "Pure bass +", "Subwoofer"),
                selectedIndex = state.bassMode,
                onSelectedIndexChange = { state.bassMode = it }
            )
            Spacer(modifier = Modifier.height(8.dp))
            ValueSlider(
                title = "Bass frequency",
                summary = (state.bassFrequency + 15).toString(),
                summaryUnit = "Hz",
                value = state.bassFrequency,
                onValueChange = { state.bassFrequency = it },
                valueRange = 0..135
            )
            ValueSlider(
                title = "Bass gain",
                summary = (state.bassGain + 1).toString(),
                summaryUnit = "dB",
                value = state.bassGain,
                onValueChange = { state.bassGain = it },
                valueRange = 0..11
            )
        }
    }
}