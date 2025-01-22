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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.lang.IllegalStateException

class GPSPointRepository {
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
        return withContext(Dispatchers.IO) {
            val currentRunSession = getCurrentSessionOrThrow()

            gpsTrackDao.getGPSTrackIdBySessionId(currentRunSession.sessionId)
                ?: throw IllegalStateException("No GPS Track ID is attached with the current run session! (GPS Point)")
        }
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

    /*suspend fun fetchTwoLatestLocation(): Flow<List<Location>> = flow {
        val gpsTrackID = getCurrentGPSTrackIDOrThrow()
        while (!gpsTrackDao.pauseOrContinueGPSTrack(gpsTrackID)) {
            try {
                val latestLocations = gpsPointDao.getTwoLatestLocation(gpsTrackID)
                emit(latestLocations)
            } catch (e: Exception) {
                println("Error fetching location (GPS Point) ${e.message}")
            }
            delay(100)
        }

    }.flowOn(Dispatchers.IO)*/

    suspend fun fetchTwoLatestLocation(): Flow<Pair<Location, Location>> = flow {
        val gpsTrackID = getCurrentGPSTrackIDOrThrow()
        val locationBuffer = mutableListOf<Location>()
        var lastTimeStamp: Long = 0

        while (!gpsTrackDao.pauseOrContinueGPSTrack(gpsTrackID)) {
            try {
                val newLocations = gpsPointDao.getNewLocations(gpsTrackID, lastTimeStamp)

                if (newLocations.isEmpty()) {
                    delay(100)
                    continue
                }

                newLocations.forEach { location ->
                    locationBuffer.add(location)
                    lastTimeStamp = maxOf(lastTimeStamp, location.timeStamp)

                    if (locationBuffer.size > 2) {
                        locationBuffer.removeAt(0)
                    }

                    if (locationBuffer.size == 2) {
                        emit(Pair(locationBuffer[0], locationBuffer[1]))
                    }
                }
            } catch (e: Exception) {
                println("Error fetching location (GPS Point): ${e.message}")
            }
        }
    }.flowOn(Dispatchers.IO)


    suspend fun fetchGPSPointList(trackId: Int): List<Location> {
        return gpsPointDao.getGPSPointByTrackId(trackId = trackId)
    }
}