package com.example.phoneusagetracker.data

import android.text.format.DateFormat
import java.util.Calendar

class UsageRepository(private val dao: UsageDao) {

    suspend fun startSession() {
        val today = getTodayDate()
        val session = UsageSession(
            sessionStartTime = System.currentTimeMillis(),
            date = today
        )
        dao.insertSession(session)
    }

    suspend fun endCurrentSession() {
        val today = getTodayDate()
        val activeSession = dao.getActiveSession(today)
        if (activeSession != null) {
            val updatedSession = activeSession.copy(sessionEndTime = System.currentTimeMillis())
            dao.updateSession(updatedSession)
            updateDailyTotal(today)
        }
    }

    suspend fun getCurrentSessionDurationMinutes(): Long {
        val today = getTodayDate()
        val activeSession = dao.getActiveSession(today)
        return if (activeSession != null) {
            (System.currentTimeMillis() - activeSession.sessionStartTime) / (1000 * 60)
        } else {
            0
        }
    }

    suspend fun getTodayTotalMinutes(): Long {
        val today = getTodayDate()
        val dailyTotal = dao.getDailyTotal(today)
        if (dailyTotal != null) {
            val activeSession = dao.getActiveSession(today)
            return if (activeSession != null) {
                val sessionDuration = (System.currentTimeMillis() - activeSession.sessionStartTime) / (1000 * 60)
                dailyTotal.totalMinutes + sessionDuration
            } else {
                dailyTotal.totalMinutes
            }
        }

        val activeSession = dao.getActiveSession(today)
        return if (activeSession != null) {
            (System.currentTimeMillis() - activeSession.sessionStartTime) / (1000 * 60)
        } else {
            0
        }
    }

    suspend fun getAllTimeTotalMinutes(): Long {
        return dao.getAllTimeTotalMinutes() ?: 0
    }

    private suspend fun updateDailyTotal(date: String) {
        val sessions = dao.getSessionsForDate(date).run {
            val result = mutableListOf<UsageSession>()
            collect { result.addAll(it) }
            result
        }

        val totalMinutes = sessions.sumOf { session ->
            val endTime = session.sessionEndTime ?: System.currentTimeMillis()
            (endTime - session.sessionStartTime) / (1000 * 60)
        }

        val existing = dao.getDailyTotal(date)
        if (existing != null) {
            dao.updateDailyTotal(existing.copy(totalMinutes = totalMinutes))
        } else {
            dao.insertDailyTotal(DailyTotal(date = date, totalMinutes = totalMinutes))
        }
    }

    private fun getTodayDate(): String {
        val calendar = Calendar.getInstance()
        return DateFormat.format("yyyy-MM-dd", calendar.time).toString()
    }
}
