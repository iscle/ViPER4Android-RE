package com.aam.viper4android.ui.effect

import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R

@Composable
fun AuditorySystemProtectionEffect() {
    var enabled by remember { mutableStateOf(false) }
    EffectCard(icon = painterResource(R.drawable.ic_protection), name = "Auditory system protection", enabled = enabled, onEnabledChange = { enabled = it })

}