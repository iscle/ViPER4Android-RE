package com.aam.viper4android.ui.effect

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R
import com.aam.viper4android.ui.ValueSlider
import com.aam.viper4android.ui.component.ValuePicker
import com.aam.viper4android.vm.ViPERBassViewModel

@Composable
fun ViPERBassEffect(
    viewModel: ViPERBassViewModel = hiltViewModel()
) {
    val enabled = viewModel.enabled.collectAsState().value
    val bassMode = viewModel.bassMode.collectAsState().value
    val bassFrequency = viewModel.bassFrequency.collectAsState().value
    val bassGain = viewModel.bassGain.collectAsState().value
    
    EffectCard(
        icon = painterResource(R.drawable.ic_bass),
        name = "ViPER bass",
        enabled = enabled,
        onEnabledChange = viewModel::setEnabled
    ) {
        Column {
            ValuePicker(
                title = "Bass mode",
                values = arrayOf("Natural bass", "Pure bass +", "Subwoofer"),
                selectedIndex = bassMode,
                onSelectedIndexChange = viewModel::setBassMode
            )
            Spacer(modifier = Modifier.height(8.dp))
            ValueSlider(
                title = "Bass frequency",
                summary = (bassFrequency + 15).toString(),
                summaryUnit = "Hz",
                value = bassFrequency,
                onValueChange = viewModel::setBassFrequency,
                valueRange = 0..135
            )
            ValueSlider(
                title = "Bass gain",
                summary = (bassGain + 1).toString(),
                summaryUnit = "dB",
                value = bassGain,
                onValueChange = viewModel::setBassGain,
                valueRange = 0..11
            )
        }
    }
}