package com.example.phoneusagetracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phoneusagetracker.data.UsageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.concurrent.timer

data class UsageStats(
    val currentSessionMinutes: Long = 0,
    val todayTotalMinutes: Long = 0,
    val allTimeTotalMinutes: Long = 0,
)

class UsageViewModel(private val repository: UsageRepository) : ViewModel() {
    private val _stats = MutableStateFlow(UsageStats())
    val stats: StateFlow<UsageStats> = _stats

    init {
        startUpdatingStats()
    }

    private fun startUpdatingStats() {
        timer(initialDelay = 1000, period = 1000) {
            viewModelScope.launch(Dispatchers.IO) {
                val currentSession = repository.getCurrentSessionDurationMinutes()
                val todayTotal = repository.getTodayTotalMinutes()
                val allTimeTotal = repository.getAllTimeTotalMinutes()

                _stats.emit(UsageStats(
                    currentSessionMinutes = currentSession,
                    todayTotalMinutes = todayTotal,
                    allTimeTotalMinutes = allTimeTotal
                ))
            }
        }
    }
}
