package com.aam.viper4android.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aam.viper4android.persistence.model.ViPERSetting

@Dao
interface SettingsDao {
    // Get all settings
    @Query("SELECT * FROM settings")
    suspend fun getAll(): List<ViPERSetting>

    // Insert setting
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(setting: ViPERSetting)

    // Get setting by key
    @Query("SELECT * FROM settings WHERE key = :key")
    suspend fun get(key: String): ViPERSetting
}