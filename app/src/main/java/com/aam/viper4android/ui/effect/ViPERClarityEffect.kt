package com.aam.viper4android.ui.effect

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R
import com.aam.viper4android.ui.ValueSlider
import com.aam.viper4android.vm.ViPERClarityViewModel

@Composable
fun ViPERClarityEffect(
    viewModel: ViPERClarityViewModel = hiltViewModel()
) {
    val enabled = viewModel.enabled.collectAsState().value
    val clarityMode = viewModel.clarityMode.collectAsState().value
    val clarityGain = viewModel.clarityGain.collectAsState().value
    
    EffectCard(
        icon = painterResource(R.drawable.ic_clarity),
        name = "ViPER clarity",
        enabled = enabled,
        onEnabledChange = viewModel::setEnabled
    ) {
        Column {
            ValueSlider(
                title = "Clarity gain",
                summaryUnit = "db",
                value = clarityGain,
                onValueChange = viewModel::setClarityGain,
                valueRange = 0..9
            )
        }
    }
}