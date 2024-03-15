package com.aam.viper4android.ui.effect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R
import com.aam.viper4android.vm.ReverberationViewModel

@Composable
fun ReverberationEffect(
    viewModel: ReverberationViewModel = viewModel()
) {
    val enabled = viewModel.enabled.collectAsState().value

    EffectCard(
        icon = painterResource(R.drawable.ic_reverb),
        name = "Reverberation",
        enabled = enabled,
        onEnabledChange = viewModel::setEnabled
    )
}