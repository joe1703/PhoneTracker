package com.example.phoneusagetracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usage_sessions")
data class UsageSession(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val sessionStartTime: Long,
    val sessionEndTime: Long? = null,
    val date: String, // YYYY-MM-DD format
)

@Entity(tableName = "daily_totals")
data class DailyTotal(
    @PrimaryKey
    val date: String, // YYYY-MM-DD format
    val totalMinutes: Long,
)
