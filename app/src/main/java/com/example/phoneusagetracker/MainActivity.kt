package com.example.phoneusagetracker

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.phoneusagetracker.data.AppDatabase
import com.example.phoneusagetracker.data.UsageRepository

class MainActivity : AppCompatActivity() {
    private lateinit var screenReceiver: ScreenReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startFloatingWindow()
        setupReceiver()
        finish()
    }

    private fun startFloatingWindow() {
        val floatingWindowIntent = Intent(this, FloatingWindowService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(floatingWindowIntent)
        } else {
            startService(floatingWindowIntent)
        }
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
}
