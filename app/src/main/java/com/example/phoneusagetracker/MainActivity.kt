package com.example.phoneusagetracker

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(this)
        } else {
            true
        }

        if (!hasPermission) {
            showPermissionRequest()
        } else {
            startFloatingWindow()
            finish()
        }
    }

    private fun showPermissionRequest() {
        setContentView(LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setPadding(20, 20, 20, 20)

            addView(TextView(this@MainActivity).apply {
                text = "Phone Usage Tracker"
                textSize = 20f
                setPadding(0, 20, 0, 20)
            })

            addView(TextView(this@MainActivity).apply {
                text = "This app needs permission to display overlay on your screen."
                textSize = 14f
                setPadding(0, 10, 0, 20)
            })

            addView(Button(this@MainActivity).apply {
                text = "Grant Permission"
                setOnClickListener {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName")
                    )
                    startActivity(intent)
                }
            })
        })
    }

    override fun onResume() {
        super.onResume()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(this)) {
                startFloatingWindow()
                finish()
            }
        }
    }

    private fun startFloatingWindow() {
        val floatingWindowIntent = Intent(this, FloatingWindowService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(floatingWindowIntent)
        } else {
            startService(floatingWindowIntent)
        }
    }
}
