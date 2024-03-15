package com.aam.viper4android.ui.effect

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R
import com.aam.viper4android.ui.ValueSlider
import com.aam.viper4android.vm.DifferentialSurroundViewModel

@Composable
fun DifferentialSurroundEffect(
    viewModel: DifferentialSurroundViewModel = hiltViewModel()
) {
    val enabled = viewModel.enabled.collectAsState().value
    val delay = viewModel.delay.collectAsState().value

    EffectCard(
        icon = painterResource(R.drawable.ic_surround),
        name = "Differential surround",
        enabled = enabled,
        onEnabledChange = viewModel::setEnabled
    ) {
        Column {
            ValueSlider(
                title = "Delay",
                summary = (delay + 1).toString(),
                summaryUnit = "ms",
                value = delay,
                onValueChange = viewModel::setDelay,
                valueRange = 0..19
            )
        }
    }
}