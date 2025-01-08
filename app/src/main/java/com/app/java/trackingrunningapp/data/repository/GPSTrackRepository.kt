package com.app.java.trackingrunningapp.data.repository

import android.util.Log
import com.app.java.trackingrunningapp.data.dao.RunSessionDao
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.model.dataclass.location.Location
import com.app.java.trackingrunningapp.data.model.entity.GPSTrack
import com.app.java.trackingrunningapp.data.model.entity.RunSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class GPSTrackRepository {
    val db = InitDatabase.runningDatabase

    private val gpsTrackDao = db.GPSTrackDao()
    private val runSessionDao: RunSessionDao = db.runSessionDao()

    private val gpsPointRepository: GPSPointRepository = InitDatabase.gpsPointRepository

    private suspend fun getCurrentRunSessionOrThrow(): RunSession {
        val currentRunSession = runSessionDao.getCurrentRunSession()
        return currentRunSession ?: throw IllegalStateException("Value of current run session is null! (GPS Track)")
    }

    private suspend fun getCurrentGPSTrackIDOrThrow(): Int {
        val currentRunSession = getCurrentRunSessionOrThrow()
        return gpsTrackDao.getGPSTrackIdBySessionId(currentRunSession.sessionId) ?: throw java.lang.IllegalStateException("No GPS Track ID is attached with the current run session! (GPS Track)")
    }

    suspend fun createGPSTrack() {
        withContext(Dispatchers.IO) {
            val currentRunSession = getCurrentRunSessionOrThrow()

            val newGPSTrack = GPSTrack(
                gpsSessionId = currentRunSession.sessionId,
                isPaused = false
            )
            gpsTrackDao.createGPSTrack(newGPSTrack)
            delay(100)
            val gpsTrackId = gpsTrackDao.getGPSTrackIdBySessionId(newGPSTrack.gpsSessionId)
                ?: throw IllegalStateException("GPS Track insertion was not successful.")
        }
    }

    suspend fun resumeGPSTrack() {
        val currentGpsTrackId = getCurrentGPSTrackIDOrThrow()
        gpsTrackDao.setGPSTrackActive(currentGpsTrackId)
    }

    suspend fun stopGPSTrack() {
        val currentGpsTrackId = getCurrentGPSTrackIDOrThrow()
        gpsTrackDao.setGPSTrackInactive(currentGpsTrackId)
    }

    suspend fun fetchGPSPointOfSession(sessionId: Int): List<Location> {
        val gpsTrackId = gpsTrackDao.getGPSTrackIdBySessionId(sessionId)
        return if (gpsTrackId != null) {
            gpsPointRepository.fetchGPSPointList(gpsTrackId)
        } else {
            emptyList()
        }
    }
}