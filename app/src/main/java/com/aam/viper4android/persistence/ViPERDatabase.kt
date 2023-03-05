package com.aam.viper4android.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aam.viper4android.persistence.model.SavedSession
import com.aam.viper4android.persistence.model.ViPERSetting

@Database(entities = [ViPERSetting::class, SavedSession::class], version = 1)
abstract class ViPERDatabase : RoomDatabase() {
    abstract fun settingsDao(): SettingsDao
    abstract fun sessionDao(): SessionDao
}