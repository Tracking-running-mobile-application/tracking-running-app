package com.app.java.trackingrunningapp.data.repository

import com.app.java.trackingrunningapp.data.dao.RunSessionDao
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.model.entity.GPSTrack
import com.app.java.trackingrunningapp.data.model.entity.RunSession

class GPSTrackRepository {
    val db = InitDatabase.runningDatabase

    private val gpsTrackDao = db.GPSTrackDao()
    private val runSessionDao: RunSessionDao = db.runSessionDao()

    private suspend fun getCurrentRunSessionOrThrow(): RunSession {
        val currentRunSession = runSessionDao.getCurrentRunSession()
        return currentRunSession ?: throw IllegalStateException("Value of current run session is null! (GPS Track)")
    }

    private suspend fun getCurrentGPSTrackIDOrThrow(): Int {
        val currentRunSession = getCurrentRunSessionOrThrow()
        return gpsTrackDao.getGPSTrackIdBySessionId(currentRunSession.sessionId) ?: throw java.lang.IllegalStateException("No GPS Track ID is attached with the current run session! (GPS Track)")
    }

    suspend fun createGPSTrack() {
        val currentRunSession = getCurrentRunSessionOrThrow()

        val newGPSTrack = GPSTrack(
            gpsSessionId = currentRunSession.sessionId,
            isPaused = false
        )

        gpsTrackDao.createGPSTrack(newGPSTrack)
    }

    suspend fun resumeGPSTrack() {
        val currentGpsTrackId = getCurrentGPSTrackIDOrThrow()
        gpsTrackDao.setGPSTrackActive(currentGpsTrackId)
    }

    suspend fun stopGPSTrack() {
        val currentGpsTrackId = getCurrentGPSTrackIDOrThrow()
        gpsTrackDao.setGPSTrackInactive(currentGpsTrackId)
    }
}