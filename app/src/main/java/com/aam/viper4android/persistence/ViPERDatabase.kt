package com.aam.viper4android.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aam.viper4android.persistence.model.PersistedSession
import com.aam.viper4android.persistence.model.PersistedSetting

@Database(entities = [PersistedSetting::class, PersistedSession::class], version = 1)
abstract class ViPERDatabase : RoomDatabase() {
    abstract fun settingsDao(): SettingsDao
    abstract fun sessionsDao(): SessionsDao
}