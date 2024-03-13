package com.aam.viper4android.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aam.viper4android.persistence.model.PersistedPreset
import com.aam.viper4android.persistence.model.PersistedSession
import com.aam.viper4android.persistence.model.PersistedSetting

@Database(entities = [PersistedSetting::class, PersistedSession::class, PersistedPreset::class], version = 1)
abstract class ViPERDatabase : RoomDatabase() {
    abstract fun settingsDao(): SettingsDao
    abstract fun sessionsDao(): SessionDao
    abstract fun presetsDao(): PresetsDao
}