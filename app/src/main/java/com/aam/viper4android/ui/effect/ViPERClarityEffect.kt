package com.aam.viper4android.ui.effect

import androidx.compose.foundation.layout.Arrangement
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
import com.aam.viper4android.vm.ViPERClarityViewModel

private val gainSummaryValues = arrayOf(
    "0.0",
    "3.5",
    "6.0",
    "8.0",
    "10.0",
    "11.0",
    "12.0",
    "13.0",
    "14.0",
    "14.8",
)

@Composable
fun ViPERClarityEffect(
    viewModel: ViPERClarityViewModel = hiltViewModel()
) {
    val enabled = viewModel.enabled.collectAsState().value
    val mode = viewModel.mode.collectAsState().value
    val gain = viewModel.gain.collectAsState().value
    
    EffectCard(
        icon = painterResource(R.drawable.ic_clarity),
        name = stringResource(R.string.viper_clarity),
        enabled = enabled,
        onEnabledChange = viewModel::setEnabled
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ValuePicker(
                title = stringResource(R.string.clarity_mode),
                values = arrayOf(
                    stringResource(R.string.natural),
                    stringResource(R.string.ozone_plus),
                    stringResource(R.string.xhifi),
                ),
                selectedIndex = mode,
                onSelectedIndexChange = viewModel::setMode
            )
            ValueSlider(
                title = stringResource(R.string.clarity_gain),
                summary = gainSummaryValues[gain],
                summaryUnit = "db",
                value = gain,
                onValueChange = viewModel::setGain,
                valueRange = gainSummaryValues.indices
            )
        }
    }
}