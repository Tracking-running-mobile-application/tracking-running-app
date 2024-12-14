package com.app.java.trackingrunningapp.model.repositories

import com.app.java.trackingrunningapp.model.DAOs.GPSPointDao
import com.app.java.trackingrunningapp.model.DAOs.GPSTrackDao
import com.app.java.trackingrunningapp.model.DAOs.RunSessionDao
import com.app.java.trackingrunningapp.model.database.InitDatabase
import com.app.java.trackingrunningapp.model.entities.GPSPoint
import com.app.java.trackingrunningapp.model.entities.RunSession
import com.app.java.trackingrunningapp.model.models.Location
import com.app.java.trackingrunningapp.ui.utils.DateTimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.IllegalStateException

class GPSPointRepository {
    val db = InitDatabase.runningDatabase

    private var fetchingJob: Job? = null

    private val gpsPointDao: GPSPointDao = db.GPSPointDao()
    private val gpsTrackDao: GPSTrackDao = db.GPSTrackDao()
    private val runSessionDao: RunSessionDao = db.runSessionDao()

    private suspend fun getCurrentSessionOrThrow(): RunSession {
        val currentRunSession = runSessionDao.getCurrentRunSession()
        return currentRunSession ?: throw IllegalStateException("Value of current run session is null! (GPS Point)")
    }

    private suspend fun getCurrentGPSTrackIDOrThrow(): Int {
        val currentRunSession = getCurrentSessionOrThrow()
        return gpsTrackDao.getGPSTrackIdBySessionId(currentRunSession.sessionId) ?: throw IllegalStateException("No GPS Track ID is attached with the current run session! (GPS Point)")
    }

    suspend fun insertGPSPoint(
        longitude: Double,
        latitude: Double
    ) {
        val currentInstant = DateTimeUtils.getCurrentInstant()
        val currentGPSTrackId = getCurrentGPSTrackIDOrThrow()

        val newGPSPoint = GPSPoint (
            gpsPointId = 0,
            trackId = currentGPSTrackId,
            longitude = longitude,
            latitude = latitude,
            timeStamp = currentInstant.toEpochMilliseconds()
        )

        gpsPointDao.insertGPSPoint(newGPSPoint)
    }

    suspend fun fetchTwoLatestLocation(): Flow<List<Location>> = flow {
        val gpsTrackID = getCurrentGPSTrackIDOrThrow()

        while (gpsTrackDao.pauseOrContinueGPSTrack(gpsTrackID)) {
            try {
                val latestLocations = gpsPointDao.getTwoLatestLocation(gpsTrackID)
                emit(latestLocations)
            } catch (e: Exception) {
                println("Error fetching location ${e.message}")
            }
            delay(3000)
        }

    }.flowOn(Dispatchers.IO)


}