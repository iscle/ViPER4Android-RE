package com.aam.viper4android.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aam.viper4android.persistence.model.PersistedSetting

@Dao
interface SettingsDao {
    // Get all settings
    @Query("SELECT * FROM settings")
    suspend fun getAll(): List<PersistedSetting>

    // Insert setting
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(setting: PersistedSetting)

    // Get setting by key
    @Query("SELECT * FROM settings WHERE key = :key")
    suspend fun get(key: String): PersistedSetting
}