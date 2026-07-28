package com.example.phoneusagetracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UsageDao {
    @Insert
    suspend fun insertSession(session: UsageSession)

    @Update
    suspend fun updateSession(session: UsageSession)

    @Query("SELECT * FROM usage_sessions WHERE date = :date ORDER BY sessionStartTime DESC")
    fun getSessionsForDate(date: String): Flow<List<UsageSession>>

    @Query("SELECT * FROM usage_sessions WHERE date = :date AND sessionEndTime IS NULL LIMIT 1")
    suspend fun getActiveSession(date: String): UsageSession?

    @Query("SELECT * FROM usage_sessions ORDER BY sessionStartTime DESC LIMIT 1")
    suspend fun getLastSession(): UsageSession?

    @Insert
    suspend fun insertDailyTotal(dailyTotal: DailyTotal)

    @Update
    suspend fun updateDailyTotal(dailyTotal: DailyTotal)

    @Query("SELECT * FROM daily_totals WHERE date = :date")
    suspend fun getDailyTotal(date: String): DailyTotal?

    @Query("SELECT SUM(totalMinutes) FROM daily_totals")
    suspend fun getAllTimeTotalMinutes(): Long?

    @Query("SELECT * FROM daily_totals ORDER BY date DESC LIMIT 30")
    fun getLast30Days(): Flow<List<DailyTotal>>
}
