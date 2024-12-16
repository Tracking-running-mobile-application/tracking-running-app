package com.app.java.trackingrunningapp.data.database

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.app.java.trackingrunningapp.data.repository.GPSPointRepository
import com.app.java.trackingrunningapp.data.repository.GPSTrackRepository
import com.app.java.trackingrunningapp.data.repository.RunSessionRepository
import com.app.java.trackingrunningapp.model.repositories.NotificationRepository
import com.app.java.trackingrunningapp.ui.viewmodel.GPSTrackViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InitDatabase : Application() {
    companion object {
        lateinit var runningDatabase: RunningDatabase
        lateinit var notificationRepository: NotificationRepository
        lateinit var runSessionRepository: RunSessionRepository
        lateinit var gpsPointRepository: GPSPointRepository
        lateinit var gpsTrackRepository: GPSTrackRepository
        lateinit var runSessionViewModel: RunSessionViewModel
        lateinit var gpsTrackViewModel: GPSTrackViewModel
    }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            runningDatabase = RunningDatabase.getInstance(this@InitDatabase)
            Log.d("Database operation", "Database initialized successfully")

            notificationRepository = NotificationRepository()
            gpsPointRepository = GPSPointRepository()
            gpsTrackRepository = GPSTrackRepository()
            runSessionRepository = RunSessionRepository(gpsPointRepository)

            runSessionViewModel = RunSessionViewModel(runSessionRepository)
            gpsTrackViewModel = GPSTrackViewModel(gpsTrackRepository)

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