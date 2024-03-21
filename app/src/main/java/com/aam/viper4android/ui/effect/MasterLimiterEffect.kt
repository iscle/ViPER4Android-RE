package com.aam.viper4android.ui.effect

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R
import com.aam.viper4android.ui.ValueSlider
import com.aam.viper4android.vm.MasterLimiterViewModel

private val outputGainSummaryValues = arrayOf(
    "-40.0",
    "-26.0",
    "-20.0",
    "-14.0",
    "-10.5",
    "-8.0",
    "-6.0",
    "-4.4",
    "-3.0",
    "-1.9",
    "-1.0",
    "0.0",
    "0.8",
    "1.6",
    "2.3",
    "2.9",
    "3.5",
    "4.1",
    "4.6",
    "5.1",
    "5.6",
    "6.0"
)

private val thresholdLimitSummaryValues = arrayOf(
    "-10.5",
    "-6.0",
    "-3.0",
    "-1.9",
    "-1.0",
    "0.0"
)

@Composable
fun MasterLimiterEffect(
    viewModel: MasterLimiterViewModel = hiltViewModel()
) {
    val outputGain = viewModel.outputGain.collectAsState().value
    val outputPan = viewModel.outputPan.collectAsState().value
    val thresholdLimit = viewModel.thresholdLimit.collectAsState().value
    
    EffectCard(
        icon = painterResource(R.drawable.ic_power),
        name = "Master limiter",
        enabled = null,
        onEnabledChange = null
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ValueSlider(
                title = "Output gain",
                summary = outputGainSummaryValues[outputGain],
                summaryUnit = "dB",
                value = outputGain,
                onValueChange = viewModel::setOutputGain,
                valueRange = outputGainSummaryValues.indices
            )
            ValueSlider(
                title = "Output pan",
                summary = "${100 - outputPan}:${outputPan}",
                value = outputPan,
                onValueChange = viewModel::setOutputPan,
                valueRange = 0..100
            )
            ValueSlider(
                title = "Threshold limit",
                value = thresholdLimit,
                summary = thresholdLimitSummaryValues[thresholdLimit],
                summaryUnit = "dB",
                onValueChange = viewModel::setThresholdLimit,
                valueRange = thresholdLimitSummaryValues.indices
            )
        }
    }
}