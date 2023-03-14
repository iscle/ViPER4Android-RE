package com.aam.viper4android.ui.effect

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R
import com.aam.viper4android.ui.ValueSlider

private const val TAG = "MasterLimiterEffect"

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

class MasterLimiterState {
    var outputGain by mutableStateOf(11)
    var outputPan by mutableStateOf(50)
    var thresholdLimit by mutableStateOf(5)
}

@Composable
fun MasterLimiterEffect(state: MasterLimiterState) {
    EffectCard(
        icon = painterResource(R.drawable.ic_power),
        name = "Master limiter",
        enabled = null,
        onEnabledChange = null
    ) {
        Column {
            ValueSlider(
                title = "Output gain",
                summary = outputGainSummaryValues[state.outputGain],
                summaryUnit = "dB",
                value = state.outputGain,
                onValueChange = { state.outputGain = it },
                valueRange = outputGainSummaryValues.indices
            )
            Spacer(modifier = Modifier.height(8.dp))
            ValueSlider(
                title = "Output pan",
                summary = "${100 - state.outputPan}:${state.outputPan}",
                value = state.outputPan,
                onValueChange = { state.outputPan = it },
                valueRange = 0..100
            )
            Spacer(modifier = Modifier.height(8.dp))
            ValueSlider(
                title = "Threshold limit",
                value = state.thresholdLimit,
                summary = thresholdLimitSummaryValues[state.thresholdLimit],
                summaryUnit = "dB",
                onValueChange = { state.thresholdLimit = it },
                valueRange = 0..5
            )
        }
    }
}