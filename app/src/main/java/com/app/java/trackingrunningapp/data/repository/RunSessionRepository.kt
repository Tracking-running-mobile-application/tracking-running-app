package com.app.java.trackingrunningapp.data.repository

import com.app.java.trackingrunningapp.data.dao.RunSessionDao
import com.app.java.trackingrunningapp.data.dao.UserDao
import com.app.java.trackingrunningapp.utils.LocalTimeConverter
import com.app.java.trackingrunningapp.data.model.entity.RunSession
import com.app.java.trackingrunningapp.data.model.entity.User
import com.app.java.trackingrunningapp.data.model.dataclass.location.StatsSession
import com.app.java.trackingrunningapp.utils.StatsUtils
import com.app.java.trackingrunningapp.utils.DateTimeUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant

class RunSessionRepository(
    private val gpsPointRepository: GPSPointRepository
) {
    val db = InitDatabase.runningDatabase

    val repoScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var statsJob: Job? = null

    private val runSessionDao: RunSessionDao = db.runSessionDao()
    private val userDao: UserDao = db.userDao()
    private val convert = LocalTimeConverter()

    private val userInfo = userDao.getUserInfo()

    private val _currentRunSession = MutableStateFlow<RunSession?>(null)
    val currentRunSession: StateFlow<RunSession?> = _currentRunSession

    private var offset: Int = 20
    private var limit: Int = 0

    private val _duration = MutableStateFlow("00:00")
    val duration: StateFlow<String> = _duration

    private val _pace = MutableStateFlow(0.0)
    val pace: StateFlow<Double> = _pace

    private val _caloriesBurned = MutableStateFlow(0.0)
    val caloriesBurned: StateFlow<Double> = _caloriesBurned

    private val _distance = MutableStateFlow<Double>(0.0)
    val distance: StateFlow<Double> = _distance

    private var runSessionStartTime: Instant = DateTimeUtils.getCurrentInstant()

    private fun getCurrentSessionOrThrow(): RunSession {
        return currentRunSession.value ?: throw IllegalStateException("Value of current run session is null!")
    }

    private suspend fun setCurrentRunSession() {
        val currentRunSession = runSessionDao.getCurrentRunSession()
        if (currentRunSession != null) {
            _currentRunSession.emit(currentRunSession)
        } else {
            println("No run session to initialize with!")
        }
    }

    suspend fun filterRunningSessionByDay(startDate: String, endDate: String): List<RunSession> {
        return runSessionDao.filterRunningSessionByDay(startDate, endDate)
    }

    suspend fun getAllRunSessions(fetchMore: Boolean = false): Pair<List<RunSession>, Boolean> {
        if( !fetchMore ) {
            offset = 0
        }
        val sessionList = runSessionDao.getAllRunSessions(limit, offset)
        offset += sessionList.size

        val hasMoreData = sessionList.size == limit

        return Pair(runSessionDao.getAllRunSessions(limit, offset), hasMoreData)
    }

    suspend fun getFavoriteRunSessions(): List<RunSession> {
        return runSessionDao.getAllFavoriteRunSessions()
    }

    suspend fun refreshRunSessionHistory(): List<RunSession> {
        offset = 0
        return runSessionDao.getAllRunSessions(limit, offset)
    }

    suspend fun deleteRunSession(sessionId: Int) {
         runSessionDao.deleteRunSession(sessionId)
    }

    suspend fun startRunSession() {
        val runDate = convert.fromLocalDate(DateTimeUtils.getCurrentDate())
        runSessionDao.initiateRunSession(0, runDate)

        setCurrentRunSession()
    }

    suspend fun setRunSessionInactive() {
        val currentSession = getCurrentSessionOrThrow()
        runSessionDao.setRunSessionInactive(currentSession.sessionId)

        _currentRunSession.emit(null)
    }

    suspend fun addFavoriteRunSession(sessionId: Int) {
        val today = DateTimeUtils.getCurrentDate().toString()
        runSessionDao.addFavoriteRunSession(sessionId, today)
    }

    suspend fun removeFavoriteRunSession(sessionId: Int) {
        runSessionDao.removeFavoriteRunSession(sessionId)
    }

    suspend fun fetchStatsSession(): StatsSession {
        val currentSession = getCurrentSessionOrThrow()
        return runSessionDao.fetchStatsSession(currentSession.sessionId)
    }

    suspend fun calcPace(): Double {
        val currentSession = getCurrentSessionOrThrow()

        val userUnitPreference = userInfo?.unit

        val durationInMinutes = currentSession.duration / 60

        val adjustedDistance: Double = when (userUnitPreference) {
            User.UNIT_MILE -> currentSession.distance * 0.621371f
            else -> currentSession.distance
        }

        val pace: Double = if (adjustedDistance > 0) {
            durationInMinutes / adjustedDistance
        } else {
            0.0
        }

        return pace
    }

    /*add in the viewmodel in the future to see run session detail*/
    suspend fun getRunSessionById(sessionId: Int): RunSession? {
        return runSessionDao.getRunSessionById(sessionId)
    }

    suspend fun calcCaloriesBurned(): Double {
        val userMetricPreference: String? = userInfo?.metricPreference
        val unit: String? = userInfo?.unit

        val currentSession = getCurrentSessionOrThrow()

        val adjustedWeight = when (userMetricPreference) {
            User.POUNDS -> userInfo?.weight?.times(0.45359237) ?: (50.0 * 0.45359237)
            else -> userInfo?.weight ?: 50.0
        }

        val adjustedPace: Double = when (unit) {
            User.UNIT_MILE -> currentSession.pace / 1.609344
            else -> currentSession.pace
        }

        val speedMetersPerSec: Double = 1000.0 / (adjustedPace * 60.0)

        val durationInHours = currentSession.duration / 3600.0

        val MET = when {
            speedMetersPerSec < 2.0 -> 0.5 + (speedMetersPerSec * 0.3)
            speedMetersPerSec >= 2.0 -> 1.0 + (speedMetersPerSec * 0.9)
            else -> 0.0
        }

        val caloriesBurnedPerHour = MET * adjustedWeight.toDouble()
        val caloriesBurned = caloriesBurnedPerHour * durationInHours

        return caloriesBurned
    }

    suspend fun calcDuration() {
         val currentRunSession = getCurrentSessionOrThrow()
         repoScope.launch {
            while (currentRunSession.isActive != false) {
                try {
                    val currentTime = DateTimeUtils.getCurrentInstant()
                    val formattedDuration = StatsUtils.calculateDuration(runSessionStartTime, currentTime)
                    _duration.emit(formattedDuration)

                    delay(1000)
                } catch (e: Exception) {
                    println("Error updating duration: ${e.message}")
                }
            }
        }
    }

    suspend fun calcDistance() = coroutineScope {
        val latestLocationsFlow = gpsPointRepository.fetchTwoLatestLocation()

        latestLocationsFlow.collect { latestLocations ->
            if (latestLocations.size == 2) {
                val (location1, location2) = latestLocations

                val distance = StatsUtils.haversineFormula(location1, location2)

                _distance.value = distance
            }
        }
    }

    suspend fun updateStatsSession(distance: Double, duration: Long, caloriesBurned: Double, pace: Double) {
        val currentSession = getCurrentSessionOrThrow()
        runSessionDao.updateStatsSession(currentSession.sessionId, distance, duration, caloriesBurned, pace)
    }
}