package com.app.java.trackingrunningapp.model.repositories

import android.content.Context
import com.app.java.trackingrunningapp.model.DAOs.GPSPointDao
import com.app.java.trackingrunningapp.model.DAOs.GPSTrackDao
import com.app.java.trackingrunningapp.model.DAOs.RunSessionDao
import com.app.java.trackingrunningapp.model.database.InitDatabase
import com.app.java.trackingrunningapp.model.entities.GPSPoint
import com.app.java.trackingrunningapp.model.entities.RunSession
import com.app.java.trackingrunningapp.model.utils.StatsUtils
import com.app.java.trackingrunningapp.ui.utils.DateTimeUtils
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import java.lang.IllegalStateException

class GPSPointRepository(
    runSessionRepository: RunSessionRepository
) {
    val db = InitDatabase.runningDatabase

    private val currentRunSession = runSessionRepository.currentRunSession

    private val gpsPointDao: GPSPointDao = db.GPSPointDao()
    private val gpsTrackDao: GPSTrackDao = db.GPSTrackDao()

    private val _currentDistance = MutableStateFlow<Double>(0.0)
    val currentDistance: StateFlow<Double> = _currentDistance

    private fun getCurrentSessionOrThrow(): RunSession {
        return currentRunSession.value ?: throw IllegalStateException("Value of current run session is null! (GPS Point)")
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

    private suspend fun fetchTwoLatestLocation() = flow {
        while (true) {
            val currentGPSTrackId = getCurrentGPSTrackIDOrThrow()

            val latestLocations = gpsPointDao.getTwoLatestLocation(currentGPSTrackId)
            emit(latestLocations)
            delay(3000)
        }
    }

    suspend fun calculateDistance() = coroutineScope{
        val currentGPSTrackId = getCurrentGPSTrackIDOrThrow()
        val latestLocationsFlow = fetchTwoLatestLocation()

        latestLocationsFlow.collect { latestLocations ->
            if (latestLocations.size == 2) {
                val (location1, location2) = latestLocations

                val distance = StatsUtils.haversineFormula(location1, location2)

                _currentDistance.value = distance
            }

            if (!gpsTrackDao.pauseOrContinueGPSTrack(currentGPSTrackId)) {
                this.cancel()
            }
        }
    }
}