package com.aam.viper4android.ui.effect

import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R

class AuditorySystemProtectionState {
    var enabled by mutableStateOf(false)
}

@Composable
fun AuditorySystemProtectionEffect(state: AuditorySystemProtectionState) {
    EffectCard(icon = painterResource(R.drawable.ic_protection), name = "Auditory system protection", enabled = state.enabled, onEnabledChange = { state.enabled = it })

}