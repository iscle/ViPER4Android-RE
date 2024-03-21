package com.aam.viper4android.ui.effect

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R
import com.aam.viper4android.ui.ValueSlider
import com.aam.viper4android.vm.PlaybackGainControlViewModel

@Composable
fun PlaybackGainControlEffect(
    viewModel: PlaybackGainControlViewModel = hiltViewModel()
) {
    val enabled = viewModel.enabled.collectAsState().value
//    val strength = viewModel.strength.collectAsState().value
//    val gain = viewModel.gain.collectAsState().value
//    val threshold = viewModel.threshold.collectAsState().value

    EffectCard(
        icon = painterResource(R.drawable.ic_playback_control),
        name = stringResource(R.string.playback_gain_control),
        enabled = enabled,
        onEnabledChange = viewModel::setEnabled
    ) {
        Column {
            ValueSlider(
                title = stringResource(R.string.strength),
                value = 0,
                onValueChange = { /*viewModel::setStrength*/ },
                valueRange = 0..2
            )
            ValueSlider(
                title = stringResource(R.string.maximum_gain),
                value = 3,
                onValueChange = { /*viewModel::setGain*/ },
                valueRange = 0..10
            )
            ValueSlider(
                title = stringResource(R.string.output_threshold),
                value = 3,
                onValueChange = { /*viewModel::setThreshold*/ },
                valueRange = 0..5
            )
        }
    }
}