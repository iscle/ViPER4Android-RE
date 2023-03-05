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

@Composable
fun MasterLimiterEffect() {
    EffectCard(
        icon = painterResource(R.drawable.ic_power),
        name = "Master limiter",
        enabled = null,
        onEnabledChange = null
    ) {
        var outputGain by rememberSaveable { mutableStateOf(11) } // TODO: Move to state
        var outputPan by rememberSaveable { mutableStateOf(50) } // TODO: Move to state
        var thresholdLimit by rememberSaveable { mutableStateOf(5) } // TODO: Move to state
        Column {
            ValueSlider(
                title = "Output gain",
                summary = outputGainSummaryValues[outputGain],
                summaryUnit = "dB",
                value = outputGain,
                onValueChange = { outputGain = it },
                valueRange = outputGainSummaryValues.indices
            )
            Spacer(modifier = Modifier.height(8.dp))
            ValueSlider(
                title = "Output pan",
                summary = "${100 - outputPan}:${outputPan}",
                value = outputPan,
                onValueChange = { outputPan = it },
                valueRange = 0..100
            )
            Spacer(modifier = Modifier.height(8.dp))
            ValueSlider(
                title = "Threshold limit",
                value = thresholdLimit,
                summary = thresholdLimitSummaryValues[thresholdLimit],
                summaryUnit = "dB",
                onValueChange = { thresholdLimit = it },
                valueRange = 0..5
            )
        }
    }
}