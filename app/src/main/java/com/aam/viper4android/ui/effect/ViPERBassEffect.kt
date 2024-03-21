package com.aam.viper4android.ui.effect

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R
import com.aam.viper4android.ui.ValueSlider
import com.aam.viper4android.ui.component.ValuePicker
import com.aam.viper4android.vm.ViPERBassViewModel

private val gainSummaryValues = arrayOf(
    "3.5",
    "6.0",
    "8.0",
    "10.0",
    "11.0",
    "12.0",
    "13.0",
    "14.0",
    "14.8",
    "15.6",
    "16.3",
    "17.0",
)

@Composable
fun ViPERBassEffect(
    viewModel: ViPERBassViewModel = hiltViewModel()
) {
    val enabled = viewModel.enabled.collectAsState().value
    val mode = viewModel.mode.collectAsState().value
    val frequency = viewModel.frequency.collectAsState().value
    val gain = viewModel.gain.collectAsState().value
    
    EffectCard(
        icon = painterResource(R.drawable.ic_bass),
        name = stringResource(R.string.viper_bass),
        enabled = enabled,
        onEnabledChange = viewModel::setEnabled
    ) {
        Column {
            ValuePicker(
                title = stringResource(R.string.bass_mode),
                values = arrayOf(
                    stringResource(R.string.natural_bass),
                    stringResource(R.string.pure_bass_plus),
                    stringResource(R.string.subwoofer),
                ),
                selectedIndex = mode,
                onSelectedIndexChange = viewModel::setMode
            )
            Spacer(modifier = Modifier.height(8.dp))
            ValueSlider(
                title = stringResource(R.string.bass_frequency),
                summary = frequency.toString(),
                summaryUnit = "Hz",
                value = frequency,
                onValueChange = viewModel::setFrequency,
                valueRange = 15..150
            )
            ValueSlider(
                title = stringResource(R.string.bass_gain),
                summary = gainSummaryValues[gain - 1],
                summaryUnit = "dB",
                value = gain,
                onValueChange = viewModel::setGain,
                valueRange = 1..12
            )
        }
    }
}