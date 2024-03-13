package com.aam.viper4android.ktx

import com.aam.viper4android.Preset
import com.aam.viper4android.persistence.model.PersistedPreset

fun PersistedPreset.asPreset(): Preset {
    return Preset()
}