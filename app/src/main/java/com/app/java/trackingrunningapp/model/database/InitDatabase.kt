package com.app.java.trackingrunningapp.model.database

import android.app.Application
import android.util.Log
import com.app.java.trackingrunningapp.modelbase.RunningDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InitDatabase : Application() {
    companion object {
        lateinit var runningDatabase: RunningDatabase
    }

    override fun onCreate() {
        super.onCreate()
        runningDatabase = RunningDatabase.getInstance(this)
        Log.d("Database operation", "Database initialized successfully")

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