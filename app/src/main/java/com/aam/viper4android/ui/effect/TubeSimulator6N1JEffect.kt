package com.aam.viper4android.ui.effect

import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R

class TubeSimulator6N1JState {
    var enabled by mutableStateOf(false)
}

@Composable
fun TubeSimulator6N1JEffect(state: TubeSimulator6N1JState) {
    EffectCard(icon = painterResource(R.drawable.ic_tubeamp), name = "Tube simulator (6N1J)", enabled = state.enabled, onEnabledChange = { state.enabled = it })

}