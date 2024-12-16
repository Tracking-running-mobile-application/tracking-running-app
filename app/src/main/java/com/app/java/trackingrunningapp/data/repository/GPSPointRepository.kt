package com.app.java.trackingrunningapp.data.repository

import com.app.java.trackingrunningapp.data.dao.GPSPointDao
import com.app.java.trackingrunningapp.data.dao.GPSTrackDao
import com.app.java.trackingrunningapp.data.dao.RunSessionDao
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.model.entity.GPSPoint
import com.app.java.trackingrunningapp.data.model.entity.RunSession
import com.app.java.trackingrunningapp.data.model.dataclass.location.Location
import com.app.java.trackingrunningapp.utils.DateTimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.IllegalStateException

class GPSPointRepository(
) {
    val db = InitDatabase.runningDatabase

    private val gpsPointDao: GPSPointDao = db.GPSPointDao()
    private val gpsTrackDao: GPSTrackDao = db.GPSTrackDao()
    private val runSessionDao: RunSessionDao = db.runSessionDao()

    private suspend fun getCurrentSessionOrThrow(): RunSession {
        val currentRunSession = runSessionDao.getCurrentRunSession()
        return currentRunSession
            ?: throw IllegalStateException("Value of current run session is null! (GPS Point)")
    }

    private suspend fun getCurrentGPSTrackIDOrThrow(): Int {
        val currentRunSession = getCurrentSessionOrThrow()
        return gpsTrackDao.getGPSTrackIdBySessionId(currentRunSession.sessionId)
            ?: throw IllegalStateException("No GPS Track ID is attached with the current run session! (GPS Point)")
    }

    suspend fun insertGPSPoint(
        longitude: Double,
        latitude: Double
    ) {
        val currentInstant = DateTimeUtils.getCurrentInstant()
        val currentGPSTrackId = getCurrentGPSTrackIDOrThrow()

        val newGPSPoint = GPSPoint(
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
                println("Error fetching location (GPS Point) ${e.message}")
            }
            delay(3000)
        }

    }.flowOn(Dispatchers.IO)


}