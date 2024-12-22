package com.app.java.trackingrunningapp.data.repository

import android.util.Log
import com.app.java.trackingrunningapp.data.dao.RunSessionDao
import com.app.java.trackingrunningapp.data.dao.UserDao
import com.app.java.trackingrunningapp.utils.LocalTimeConverter
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.model.entity.RunSession
import com.app.java.trackingrunningapp.data.model.entity.User
import com.app.java.trackingrunningapp.data.model.dataclass.location.StatsSession
import com.app.java.trackingrunningapp.utils.StatsUtils
import com.app.java.trackingrunningapp.utils.DateTimeUtils
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import java.util.Date

class RunSessionRepository(
    private val gpsPointRepository: GPSPointRepository
) {
    val db = InitDatabase.runningDatabase

    private val runSessionDao: RunSessionDao = db.runSessionDao()
    private val userDao: UserDao = db.userDao()
    private val convert = LocalTimeConverter()

    private val userInfo = userDao.getUserInfo()

    private val _currentRunSession = MutableStateFlow<RunSession?>(null)
    val currentRunSession: StateFlow<RunSession?> = _currentRunSession

    private var offset: Int = 20
    private var limit: Int = 0

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration

    private var cumulativeDurationSeconds: Long = 0L

    private val _pace = MutableStateFlow(0.0)
    val pace: StateFlow<Double> = _pace

    private val _caloriesBurned = MutableStateFlow(0.0)
    val caloriesBurned: StateFlow<Double> = _caloriesBurned

    private val _distance = MutableStateFlow<Double>(0.0)
    val distance: StateFlow<Double> = _distance

    private lateinit var runSessionStartTime: Instant

    private var repoScope = CoroutineScope(Job() + Dispatchers.IO)

    private fun getCurrentSessionOrThrow(): RunSession {
        return currentRunSession.value ?: throw IllegalStateException("Value of current run session is null! (RunSession Repository)")
    }

    private suspend fun setCurrentRunSession(): RunSession? {
        val currentRunSession = runSessionDao.getCurrentRunSession()
        Log.d("Run SessionRepository", "Set current run session ! ${currentRunSession?.isActive}, ${currentRunSession?.sessionId}")
        if (currentRunSession != null) {
            _currentRunSession.emit(currentRunSession)
            return currentRunSession
        } else {
            println("No run session to initialize with!")
            return null
        }
    }

    private fun ensureRepoScope() {
        if (!repoScope.isActive) {
            repoScope = CoroutineScope(Job() + Dispatchers.IO)
        }
    }

    suspend fun resetStatsValue() {
        Log.d("resetStatsValue", "Function called")
        _duration.value = 0L
        _distance.value = 0.0
        _pace.value = 0.0
        _caloriesBurned.value = 0.0
        Log.d("resetStatsValue", "Values reset: ${_duration.value}, ${_distance.value}, ${_pace.value}, ${_caloriesBurned.value}")
    }

    suspend fun stopUpdatingStats() {
        repoScope.cancel()
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

    fun setRunSessionStartTime() {
        val currentRunSession = getCurrentSessionOrThrow()
        runSessionStartTime = DateTimeUtils.getCurrentInstant()
        cumulativeDurationSeconds += currentRunSession.duration ?: 0L
        Log.d("setRunSessionStartTime", "Cumulative Duration Updated: $cumulativeDurationSeconds")
    }

    suspend fun startRunSession() {
        cumulativeDurationSeconds = 0
        val runDate = convert.fromLocalDate(DateTimeUtils.getCurrentDate())

        val newRunSession = RunSession(
            runDate = runDate,
            distance = 0.0,
            duration = 0L,
            pace = 0.0,
            caloriesBurned = 0.0,
            isActive = true,
            dateAddInFavorite = null,
            isFavorite = false
        )

        runSessionDao.initiateRunSession(newRunSession)
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

    suspend fun calcPace() {
        ensureRepoScope()
        repoScope.launch {
            try {
                Log.d("Run Session Repo", "update pace")
                val currentSession = getCurrentSessionOrThrow()

                val userUnitPreference = userInfo?.unit

                val durationInMinutes = currentSession.duration?.div(60)

                val adjustedDistance: Double? = when (userUnitPreference) {
                    User.UNIT_MILE -> currentSession.distance?.times(0.621371f)
                    else -> currentSession.distance
                }

                val pace: Double? = if (adjustedDistance != null && adjustedDistance > 0) {
                    durationInMinutes?.div(adjustedDistance)
                } else {
                    0.0
                }

                if (pace != null) {
                    _pace.emit(pace)
                } else {
                    _pace.emit(0.0)
                }
                delay(100)
            } catch (ce: CancellationException) {
                println("calcPace runSessionRepo: ${ce.message} ")
            }
        }
    }

    /*add in the viewmodel in the future to see run session detail*/
    suspend fun getRunSessionById(sessionId: Int): RunSession? {
        return runSessionDao.getRunSessionById(sessionId)
    }

    suspend fun calcCaloriesBurned() {
        ensureRepoScope()
        repoScope.launch {
            try {
                val userMetricPreference: String? = userInfo?.metricPreference
                val unit: String? = userInfo?.unit

                Log.d("Run Session Repo", "update calories")
                val currentSession = getCurrentSessionOrThrow()

                val adjustedWeight = when (userMetricPreference) {
                    User.POUNDS -> userInfo?.weight?.times(0.45359237) ?: (50.0 * 0.45359237)
                    else -> userInfo?.weight ?: 50.0
                }

                val adjustedPace: Double? = when (unit) {
                    User.UNIT_MILE -> currentSession.pace?.div(1.609344)
                    else -> currentSession.pace
                }

                val speedMetersPerSec: Double = if (adjustedPace != null && adjustedPace > 0) {
                    1000.0 / (adjustedPace * 60.0)
                } else {
                    0.0
                }

                val durationInHours = currentSession.duration?.div(3600.0)

                val MET = when {
                    speedMetersPerSec < 2.0 -> 0.5 + (speedMetersPerSec * 0.3)
                    speedMetersPerSec >= 2.0 -> 1.0 + (speedMetersPerSec * 0.9)
                    else -> 0.0
                }

                val caloriesBurnedPerHour = MET * adjustedWeight

                val caloriesBurned: Double = if (durationInHours != null && durationInHours > 0) {
                    caloriesBurnedPerHour * durationInHours
                } else {
                    0.0
                }

                _caloriesBurned.value = caloriesBurned

                delay(100)
            } catch (ce: CancellationException) {
                println("calc CaloriesBurned runSessionRepo ${ce.message}")
            }
        }
    }

    suspend fun calcDuration() {
        ensureRepoScope()
        repoScope.launch {
            Log.d("Run Session Repo", "update duration")
            while (isActive) {
                try {
                    val currentTime = DateTimeUtils.getCurrentInstant()
                    val currentDuration = StatsUtils.calculateDuration(runSessionStartTime, currentTime)
                    val totalDurationSeconds = cumulativeDurationSeconds + currentDuration

                    Log.d("calcDuration", "Cumulative: $cumulativeDurationSeconds, Current: $currentDuration, Total: $totalDurationSeconds")

                    _duration.emit(totalDurationSeconds)

                    delay(1000)
                } catch (e: Exception) {
                    println(" calcDuration RunSessionRepo ${e.message}")
                } catch (ce: CancellationException) {
                    println("Error cancellation ${ce.message}")
                }
            }
        }
    }

    suspend fun calcDistance() {
        ensureRepoScope()
        repoScope.launch {
            try {
                Log.d("Run Session Repo", "update distance")
                val latestLocationsFlow = gpsPointRepository.fetchTwoLatestLocation()

                latestLocationsFlow.collect { latestLocations ->
                    if (latestLocations.size == 2) {
                        val (location1, location2) = latestLocations

                        val distance = StatsUtils.haversineFormula(location1, location2)
                        _distance.emit(distance)

                        delay(100)
                    }
                }
            } catch (ce: CancellationException) {
                println("calcDistance runSessionRepo ${ce.message}")
            }
        }
    }

    suspend fun updateStatsSession(distance: Double, duration: Long, caloriesBurned: Double, pace: Double) {
        val currentSession = getCurrentSessionOrThrow()
        Log.d("Run Session Repo", "update stats repo")
        runSessionDao.updateStatsSession(currentSession.sessionId, distance, duration, caloriesBurned, pace)
    }
}