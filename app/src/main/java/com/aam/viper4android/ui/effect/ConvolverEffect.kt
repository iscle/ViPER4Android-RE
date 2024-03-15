package com.aam.viper4android.ui.effect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R
import com.aam.viper4android.vm.ConvolverViewModel

@Composable
fun ConvolverEffect(
    viewModel: ConvolverViewModel = hiltViewModel()
) {
    val enabled = viewModel.enabled.collectAsState().value

    EffectCard(
        icon = painterResource(R.drawable.ic_convolver),
        name = "Convolver",
        enabled = enabled,
        onEnabledChange = viewModel::setEnabled
    )
}