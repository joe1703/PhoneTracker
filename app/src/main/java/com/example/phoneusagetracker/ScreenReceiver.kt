package com.example.phoneusagetracker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.example.phoneusagetracker.data.AppDatabase
import com.example.phoneusagetracker.data.UsageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScreenReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val db = AppDatabase.getInstance(context)
        val repository = UsageRepository(db.usageDao())

        when (intent.action) {
            Intent.ACTION_SCREEN_ON -> {
                CoroutineScope(Dispatchers.IO).launch {
                    repository.startSession()
                }
            }
            Intent.ACTION_SCREEN_OFF -> {
                CoroutineScope(Dispatchers.IO).launch {
                    repository.endCurrentSession()
                }
            }
        }
    }

    companion object {
        fun getIntentFilter(): IntentFilter {
            return IntentFilter().apply {
                addAction(Intent.ACTION_SCREEN_ON)
                addAction(Intent.ACTION_SCREEN_OFF)
            }
        }
    }
}
