package com.aam.viper4android.ui.effect

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R
import com.aam.viper4android.ui.ValueSlider
import com.aam.viper4android.vm.SpectrumExtensionViewModel

@Composable
fun SpectrumExtensionEffect(
    viewModel: SpectrumExtensionViewModel = hiltViewModel()
) {
    val enabled = viewModel.enabled.collectAsState().value
    val strength = viewModel.strength.collectAsState().value

    EffectCard(
        icon = painterResource(R.drawable.ic_vse),
        name = "Spectrum extension",
        enabled = enabled,
        onEnabledChange = viewModel::setEnabled
    ) {
        Column {
            ValueSlider(
                title = "Strength",
                summaryUnit = "%",
                value = strength,
                onValueChange = viewModel::setStrength,
                valueRange = 0..100
            )
        }
    }
}