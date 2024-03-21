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
import com.aam.viper4android.vm.HeadphoneSurroundPlusViewModel

@Composable
fun HeadphoneSurroundPlusEffect(
    viewModel: HeadphoneSurroundPlusViewModel = hiltViewModel()
) {
    val enabled = viewModel.enabled.collectAsState().value
    val level = viewModel.level.collectAsState().value

    EffectCard(
        icon = painterResource(R.drawable.ic_headphone_surround),
        name = stringResource(R.string.headphone_surround_plus),
        enabled = enabled,
        onEnabledChange = viewModel::setEnabled
    ) {
        Column {
            ValueSlider(
                title = stringResource(R.string.level),
                summary = (level + 1).toString(),
                value = level,
                onValueChange = viewModel::setLevel,
                valueRange = 0..4
            )
        }
    }
}