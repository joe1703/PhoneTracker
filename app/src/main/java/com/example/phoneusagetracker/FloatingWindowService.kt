package com.example.phoneusagetracker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.NotificationCompat
import com.example.phoneusagetracker.data.AppDatabase
import com.example.phoneusagetracker.data.UsageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.timer

class FloatingWindowService : Service() {
    private lateinit var windowManager: WindowManager
    private var floatingView: LinearLayout? = null
    private lateinit var repository: UsageRepository
    private var isExpanded = false
    private lateinit var screenReceiver: ScreenReceiver
    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        try {
            super.onCreate()
            Log.d("FloatingWindow", "Service created")

            windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val db = AppDatabase.getInstance(this)
            repository = UsageRepository(db.usageDao())

            createNotificationChannel()
            startForegroundNotification()

            setupScreenReceiver()
            createFloatingWindow()
            startUpdatingStats()

            scope.launch(Dispatchers.IO) {
                repository.startSession()
            }

            Log.d("FloatingWindow", "Service initialized successfully")
        } catch (e: Exception) {
            Log.e("FloatingWindow", "Error in onCreate", e)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "phone_tracker",
                "Phone Usage Tracker",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun startForegroundNotification() {
        val notification = NotificationCompat.Builder(this, "phone_tracker")
            .setContentTitle("Phone Usage Tracker")
            .setContentText("Tracking screen time...")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setOngoing(true)
            .build()

        startForeground(1, notification)
    }

    private fun setupScreenReceiver() {
        try {
            screenReceiver = ScreenReceiver()
            val intentFilter = ScreenReceiver.getIntentFilter()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                registerReceiver(screenReceiver, intentFilter, Context.RECEIVER_EXPORTED)
            } else {
                @Suppress("UnspecifiedRegisterReceiverFlag")
                registerReceiver(screenReceiver, intentFilter)
            }
            Log.d("FloatingWindow", "Screen receiver registered")
        } catch (e: Exception) {
            Log.e("FloatingWindow", "Error setting up receiver", e)
        }
    }

    private fun createFloatingWindow() {
        try {
            floatingView = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setBackgroundColor(0xCC6200EE.toInt())
            }

            val params = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams(
                    300, 120,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
                )
            } else {
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams(
                    300, 120,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
                )
            }

            params.gravity = Gravity.TOP or Gravity.RIGHT
            params.x = 0
            params.y = 100

            floatingView?.apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )

                val currentSessionText = TextView(this@FloatingWindowService).apply {
                    text = "Session: 0 m"
                    textSize = 14f
                    setTextColor(0xFFFFFFFF.toInt())
                    setPadding(10, 5, 10, 5)
                    tag = "session"
                }

                val todayTotalText = TextView(this@FloatingWindowService).apply {
                    text = "Today: 0 m"
                    textSize = 14f
                    setTextColor(0xFFFFFFFF.toInt())
                    setPadding(10, 5, 10, 5)
                    tag = "today"
                }

                addView(currentSessionText)
                addView(todayTotalText)

                setOnTouchListener { _, event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            isExpanded = !isExpanded
                            updateWindowSize()
                        }
                    }
                    false
                }

                windowManager.addView(this, params)
                Log.d("FloatingWindow", "Floating window created")
            }
        } catch (e: Exception) {
            Log.e("FloatingWindow", "Error creating floating window", e)
        }
    }

    private fun updateWindowSize() {
        try {
            floatingView?.let {
                val params = it.layoutParams as WindowManager.LayoutParams
                if (isExpanded) {
                    params.width = 400
                    params.height = 250
                } else {
                    params.width = 300
                    params.height = 120
                }
                windowManager.updateViewLayout(it, params)
            }
        } catch (e: Exception) {
            Log.e("FloatingWindow", "Error updating window size", e)
        }
    }

    private fun startUpdatingStats() {
        timer(initialDelay = 1000, period = 1000) {
            scope.launch {
                try {
                    val currentSession = repository.getCurrentSessionDurationMinutes()
                    val todayTotal = repository.getTodayTotalMinutes()

                    floatingView?.let {
                        val sessionText = it.findViewWithTag<TextView>("session")
                        val todayText = it.findViewWithTag<TextView>("today")

                        sessionText?.text = "Session: $currentSession m"
                        todayText?.text = "Today: $todayTotal m"
                    }
                } catch (e: Exception) {
                    Log.e("FloatingWindow", "Error updating stats", e)
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        try {
            super.onDestroy()
            floatingView?.let { windowManager.removeView(it) }
            unregisterReceiver(screenReceiver)
            Log.d("FloatingWindow", "Service destroyed")
        } catch (e: Exception) {
            Log.e("FloatingWindow", "Error in onDestroy", e)
        }
    }
}
