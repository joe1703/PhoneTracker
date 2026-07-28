package com.example.phoneusagetracker

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startFloatingWindow()
        finish()
    }

    private fun startFloatingWindow() {
        try {
            val floatingWindowIntent = Intent(this, FloatingWindowService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(floatingWindowIntent)
            } else {
                @Suppress("DEPRECATION")
                startService(floatingWindowIntent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
