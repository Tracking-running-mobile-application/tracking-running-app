package com.app.java.trackingrunningapp.data.database

import android.app.Application
import android.util.Log
import com.app.java.trackingrunningapp.model.repositories.NotificationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InitDatabase : Application() {
    companion object {
        lateinit var runningDatabase: RunningDatabase
        lateinit var notificationRepository: NotificationRepository
    }

    override fun onCreate() {
        super.onCreate()
        runningDatabase = RunningDatabase.getInstance(this)
        Log.d("Database operation", "Database initialized successfully")

        notificationRepository = NotificationRepository()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val records = runningDatabase.runSessionDao().getAllRunSessions(20, 20)
                if (records.isEmpty()) {
                    Log.d("DatabaseRecord", "No records found in RunSession table.")
                } else {
                    records.forEach { record ->
                        Log.d("DatabaseRecord", record.toString())
                    }
                }
            } catch (e: Exception) {
                Log.e("DATABASE", "ERROR QUEYING DB", e)
            }
        }
    }
}