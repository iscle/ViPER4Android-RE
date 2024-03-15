package com.aam.viper4android.ui.effect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R
import com.aam.viper4android.vm.TubeSimulator6N1JViewModel

@Composable
fun TubeSimulator6N1JEffect(
    viewModel: TubeSimulator6N1JViewModel = hiltViewModel()
) {
    val enabled = viewModel.enabled.collectAsState().value

    EffectCard(
        icon = painterResource(R.drawable.ic_tubeamp),
        name = "Tube simulator (6N1J)",
        enabled = enabled,
        onEnabledChange = viewModel::setEnabled
    )
}