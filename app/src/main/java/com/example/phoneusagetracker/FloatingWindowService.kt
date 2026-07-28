package com.example.phoneusagetracker

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
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
        super.onCreate()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val db = AppDatabase.getInstance(this)
        repository = UsageRepository(db.usageDao())
        setupScreenReceiver()
        createFloatingWindow()
        startUpdatingStats()
    }

    private fun setupScreenReceiver() {
        screenReceiver = ScreenReceiver()
        val intentFilter = ScreenReceiver.getIntentFilter()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(screenReceiver, intentFilter, Context.RECEIVER_EXPORTED)
        } else {
            @Suppress("UnspecifiedRegisterReceiverFlag")
            registerReceiver(screenReceiver, intentFilter)
        }
    }

    private fun createFloatingWindow() {
        floatingView = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(0xCC6200EE.toInt())
        }

        val params = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams(
                300, 120,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT
            )
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams(
                300, 120,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
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
            }

            val todayTotalText = TextView(this@FloatingWindowService).apply {
                text = "Today: 0 m"
                textSize = 14f
                setTextColor(0xFFFFFFFF.toInt())
                setPadding(10, 5, 10, 5)
            }

            addView(currentSessionText)
            addView(todayTotalText)

            setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        isExpanded = !isExpanded
                        updateWindowSize()
                    }
                }
                false
            }

            windowManager.addView(this, params)
        }
    }

    private fun updateWindowSize() {
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
    }

    private fun startUpdatingStats() {
        timer(initialDelay = 1000, period = 1000) {
            scope.launch {
                val currentSession = repository.getCurrentSessionDurationMinutes()
                val todayTotal = repository.getTodayTotalMinutes()

                floatingView?.let {
                    if (it.childCount >= 2) {
                        val currentSessionText = it.getChildAt(0) as? TextView
                        val todayTotalText = it.getChildAt(1) as? TextView

                        currentSessionText?.text = "Session: $currentSession m"
                        todayTotalText?.text = "Today: $todayTotal m"
                    }
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        floatingView?.let { windowManager.removeView(it) }
        unregisterReceiver(screenReceiver)
    }
}
