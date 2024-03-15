package com.aam.viper4android.ui.effect

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R
import com.aam.viper4android.ui.ValueSlider
import com.aam.viper4android.vm.AnalogXViewModel

@Composable
fun AnalogXEffect(
    viewModel: AnalogXViewModel = hiltViewModel()
) {
    val enabled = viewModel.enabled.collectAsState().value
    val level = viewModel.level.collectAsState().value

    EffectCard(
        icon = painterResource(R.drawable.ic_analogx),
        name = "AnalogX",
        enabled = enabled,
        onEnabledChange = viewModel::setEnabled
    ) {
        Column {
            ValueSlider(
                title = "Level",
                summary = (level + 1).toString(),
                value = level,
                onValueChange = viewModel::setLevel,
                valueRange = 0..2
            )
        }
    }
}