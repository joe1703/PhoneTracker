package com.example.phoneusagetracker

import android.content.Context
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.phoneusagetracker.data.AppDatabase
import com.example.phoneusagetracker.data.UsageRepository
import com.example.phoneusagetracker.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var screenReceiver: ScreenReceiver
    private val viewModel: UsageViewModel by viewModels {
        UsageViewModelFactory(UsageRepository(AppDatabase.getInstance(this).usageDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupReceiver()
        observeStats()
    }

    private fun setupReceiver() {
        screenReceiver = ScreenReceiver()
        val intentFilter = ScreenReceiver.getIntentFilter()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(screenReceiver, intentFilter, Context.RECEIVER_EXPORTED)
        } else {
            registerReceiver(screenReceiver, intentFilter)
        }
    }

    private fun observeStats() {
        lifecycleScope.launch {
            viewModel.stats.collect { stats ->
                binding.currentSessionTime.text = formatTime(stats.currentSessionMinutes)
                binding.todayTotalTime.text = formatTime(stats.todayTotalMinutes)
                binding.allTimeTotalTime.text = formatTime(stats.allTimeTotalMinutes)
            }
        }
    }

    private fun formatTime(minutes: Long): String {
        val hours = minutes / 60
        val mins = minutes % 60
        return when {
            hours > 0 -> "$hours h $mins m"
            else -> "$mins m"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(screenReceiver)
    }
}
