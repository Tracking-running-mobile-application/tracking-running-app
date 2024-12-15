package com.app.java.trackingrunningapp.data.repository

import com.app.java.trackingrunningapp.data.dao.RunSessionDao
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.model.entity.RunSession
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class GPSTrackRepository {
    val db = InitDatabase.runningDatabase

    private val gpsTrackDao = db.GPSTrackDao()
    private val runSessionDao: RunSessionDao = db.runSessionDao()

    private val gpsTrackMutex = Mutex()

    private suspend fun getCurrentRunSessionOrThrow(): RunSession {
        val currentRunSession = runSessionDao.getCurrentRunSession()
        return currentRunSession ?: throw IllegalStateException("Value of current run session is null! (GPS Track)")
    }

    private suspend fun getCurrentGPSTrackIDOrThrow(): Int {
        val currentRunSession = getCurrentRunSessionOrThrow()
        return gpsTrackDao.getGPSTrackIdBySessionId(currentRunSession.sessionId) ?: throw java.lang.IllegalStateException("No GPS Track ID is attached with the current run session! (GPS Point)")
    }

    suspend fun createGPSTrack() {
        val currentRunSession = getCurrentRunSessionOrThrow()

        gpsTrackMutex.withLock {
            gpsTrackDao.createGPSTrack(
                gpsTrackId = 0,
                gpsSessionId = currentRunSession.sessionId
            )
        }
    }

    suspend fun startGPSTrack() {
        gpsTrackMutex.withLock {
            val currentGpsTrackId = getCurrentGPSTrackIDOrThrow()
            gpsTrackDao.setGPSTrackActive(currentGpsTrackId)
        }
    }

    suspend fun stopGPSTrack() {
        val currentGpsTrackId = getCurrentGPSTrackIDOrThrow()
        gpsTrackDao.setGPSTrackInactive(currentGpsTrackId)
    }
}