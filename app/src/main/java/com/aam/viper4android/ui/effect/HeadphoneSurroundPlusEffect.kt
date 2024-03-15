package com.aam.viper4android.ui.effect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R
import com.aam.viper4android.vm.HeadphoneSurroundPlusViewModel

@Composable
fun HeadphoneSurroundPlusEffect(
    viewModel: HeadphoneSurroundPlusViewModel = hiltViewModel()
) {
    val enabled = viewModel.enabled.collectAsState().value

    EffectCard(
        icon = painterResource(R.drawable.ic_headphone_surround),
        name = "Headphone surround +",
        enabled = enabled,
        onEnabledChange = viewModel::setEnabled
    )
}