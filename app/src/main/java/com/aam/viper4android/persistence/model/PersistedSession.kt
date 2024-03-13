package com.aam.viper4android.persistence.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "sessions", primaryKeys = ["package_name", "session_id"])
data class PersistedSession(
    @ColumnInfo(name = "package_name") val packageName: String,
    @ColumnInfo(name = "session_id") val sessionId: Int,
    @ColumnInfo(name = "boot_count") val bootCount: Int,
)
