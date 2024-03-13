package com.aam.viper4android.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aam.viper4android.persistence.model.PersistedPreset
import com.aam.viper4android.persistence.model.PersistedSetting

@Dao
interface PresetsDao {
    // Get a preset by device id
    @Query("SELECT * FROM presets WHERE device_id = :deviceId")
    suspend fun get(deviceId: String): PersistedPreset?

    // Insert preset
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(preset: PersistedPreset)
}