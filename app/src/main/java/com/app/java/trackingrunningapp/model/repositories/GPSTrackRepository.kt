package com.app.java.trackingrunningapp.model.repositories

import com.app.java.trackingrunningapp.model.database.InitDatabase
import com.app.java.trackingrunningapp.model.entities.RunSession

class GPSTrackRepository (
    runSessionRepository: RunSessionRepository
) {
    val db = InitDatabase.runningDatabase

    private val gpsTrackDao = db.GPSTrackDao()
    private val currentRunSession = runSessionRepository.currentRunSession

    private fun getCurrentRunSessionOrThrow(): RunSession {
        return currentRunSession.value ?: throw IllegalStateException("Value of current run session is null! (GPS Track)")
    }

    private suspend fun getCurrentGPSTrackIDOrThrow(): Int {
        val currentRunSession = getCurrentRunSessionOrThrow()
        return gpsTrackDao.getGPSTrackIdBySessionId(currentRunSession.sessionId) ?: throw java.lang.IllegalStateException("No GPS Track ID is attached with the current run session! (GPS Point)")
    }

    suspend fun initiateGPSTrack() {
        val currentRunSession = getCurrentRunSessionOrThrow()

        gpsTrackDao.insertGPSTrack(
            gpsTrackId = 0,
            gpsSessionId = currentRunSession.sessionId
        )
    }

    suspend fun startGPSTrack() {
        val currentGpsTrackId = getCurrentGPSTrackIDOrThrow()
        gpsTrackDao.continueGPSTrack(currentGpsTrackId)
    }

    suspend fun stopGPSTrack() {
        val currentGpsTrackId = getCurrentGPSTrackIDOrThrow()
        gpsTrackDao.pauseGPSTrack(currentGpsTrackId)
    }
}