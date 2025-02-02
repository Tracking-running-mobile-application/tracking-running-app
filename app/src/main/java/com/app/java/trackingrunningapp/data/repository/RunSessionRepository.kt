package com.app.java.trackingrunningapp.data.repository

import android.util.Log
import com.app.java.trackingrunningapp.data.dao.RunSessionDao
import com.app.java.trackingrunningapp.data.dao.UserDao
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.model.entity.RunSession
import com.app.java.trackingrunningapp.data.model.entity.User
import com.app.java.trackingrunningapp.data.model.dataclass.location.StatsSession
import com.app.java.trackingrunningapp.model.repositories.NotificationRepository
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

class RunSessionRepository {
    val db = InitDatabase.runningDatabase

    private val runSessionDao: RunSessionDao = db.runSessionDao()
    private val userDao: UserDao = db.userDao()

    private val gpsPointRepository: GPSPointRepository = InitDatabase.gpsPointRepository
    private val notificationRepository: NotificationRepository = InitDatabase.notificationRepository

    private val userInfo = userDao.getUserInfo()

    private val _currentRunSession = MutableStateFlow<RunSession?>(null)
    val currentRunSession: StateFlow<RunSession?> = _currentRunSession

    private var offset: Int = 0

    private var durationNotification: Boolean = false
    private var paceNotification: Boolean = false

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration

    private var cumulativeDurationSeconds: Long = 0L
    private var totalDurationSeconds: Long = 0L

    private val _speed = MutableStateFlow(0.0)
    val speed: StateFlow<Double> = _speed

    private val _caloriesBurned = MutableStateFlow(0.0)
    val caloriesBurned: StateFlow<Double> = _caloriesBurned

    private val _distance = MutableStateFlow<Double>(0.0)
    val distance: StateFlow<Double> = _distance

    private lateinit var runSessionStartTime: Instant
    private var newDistance: Double = 0.0

    private var currentGPSPointId: Int = 0

    private var repoScope = CoroutineScope(Job() + Dispatchers.IO)

    private fun getCurrentSessionOrThrow(): RunSession {
        return currentRunSession.value ?: throw IllegalStateException("Value of current run session is null! (RunSession Repository)")
    }

    private suspend fun setCurrentRunSession(): RunSession? {
        val currentRunSession = runSessionDao.getCurrentRunSession()
        Log.d("setCurrentRunSession", "${currentRunSession?.sessionId}")
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

    fun resetStatsValue() {
        _duration.value = 0L
        _distance.value = 0.0
        _speed.value = 0.0
        _caloriesBurned.value = 0.0
        cumulativeDurationSeconds = 0L
        totalDurationSeconds = 0L
        newDistance = 0.0
        currentGPSPointId = 0
        durationNotification = false
        paceNotification = false
        runSessionStartTime = DateTimeUtils.getCurrentInstant()
    }

    fun stopUpdatingStats() {
        repoScope.cancel()
    }

    suspend fun filterRunningSessionByDay(startDate: String, endDate: String): List<RunSession> {
        return runSessionDao.filterRunningSessionByDay(startDate, endDate)
    }

    suspend fun getAllRunSessions(fetchMore: Boolean = false): Pair<List<RunSession>, Boolean> {
        if( !fetchMore ) {
            offset = 0
        }
        val sessionList = runSessionDao.getAllRunSessions(21, offset)
        offset += sessionList.size

        val hasMoreData = sessionList.size == 21

        val finalList = if (hasMoreData) {
            sessionList.dropLast(1)
        } else {
            sessionList
        }

        if (fetchMore) {
            offset += 20
        }

        return Pair(finalList, hasMoreData)
    }

    suspend fun getFavoriteRunSessions(): List<RunSession> {
        return runSessionDao.getAllFavoriteRunSessions()
    }

    suspend fun refreshRunSessionHistory(): List<RunSession> {
        offset = 0
        return runSessionDao.getAllRunSessions(20, 0)
    }

    suspend fun deleteRunSession(sessionId: Int) {
         runSessionDao.deleteRunSession(sessionId)
    }

    fun setRunSessionStartTime() {
        runSessionStartTime = DateTimeUtils.getCurrentInstant()
    }
    fun pauseDuration() {
        val currentTime = DateTimeUtils.getCurrentInstant()
        val elapsedDuration = StatsUtils.calculateDuration(runSessionStartTime, currentTime)
        cumulativeDurationSeconds += elapsedDuration
    }


    suspend fun startRunSession() {
        val runDate = DateTimeUtils.formatDateStringRemoveHyphen(DateTimeUtils.getCurrentDate().toString())

        val newRunSession = RunSession(
            runDate = runDate,
            distance = 0.0,
            duration = 0L,
            speed = 0.0,
            caloriesBurned = 0.0,
            isActive = true,
            dateAddInFavorite = null,
            isFavorite = false
        )

        runSessionDao.initiateRunSession(newRunSession)
        setCurrentRunSession()
    }

    suspend fun setRunSessionInactive() {
        Log.d("RunSessionRepo", "Set Run Session Inactive")
        _currentRunSession.value?.let { runSessionDao.setRunSessionInactive(it.sessionId) }

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
                Log.d("RunSessionRepo", "calc Pace")
                val userMetricPreference = userInfo?.metricPreference

                val durationInHours = _duration.value.div(3600.0)

                val speed: Double = if (_distance.value > 0) {
                    _distance.value.div(durationInHours)
                } else {
                    0.0
                }

                val excessivePace = when (userMetricPreference) {
                    User.UNIT_MILE -> speed > 30.0
                    else -> speed > 45.0
                }

                if (excessivePace && !paceNotification) {
                    notificationRepository.triggerNotification("EXCESSIVE_SPEED")
                    paceNotification = true
                }

                _speed.emit(speed)
                delay(100)
            } catch (ce: CancellationException) {
                println("calcPace runSessionRepo: ${ce.message} ")
            }
        }
    }

    suspend fun calcCaloriesBurned() {
        ensureRepoScope()
        repoScope.launch {
            try {
                Log.d("RunSessionRepo", "calc calories burend")
                val userMetricPreference: String? = userInfo?.metricPreference
                val unit: String? = userInfo?.unit

                val isTooSlow = when (userMetricPreference) {
                    User.UNIT_MILE -> _speed.value < 3.5
                    else -> _speed.value < 4.0
                }

                if(isTooSlow) {
                    return@launch
                }

                val adjustedWeight = when (unit) {
                    User.POUNDS -> userInfo?.weight?.times(0.45359237) ?: (50.0 * 0.45359237)
                    else -> userInfo?.weight ?: 50.0
                }

                val durationInHours = _duration.value.div(3600.0)

                val MET = if (userMetricPreference == User.UNIT_KM) {
                    when {
                        _speed.value == 0.0 -> 0.0
                        _speed.value < 4.8 -> 4.5
                        _speed.value in 4.8..6.4 -> 6.2
                        _speed.value in 6.4..8.0 -> 8.5
                        _speed.value in 8.0..9.7 -> 10.0
                        _speed.value in 9.7..11.3 -> 11.2
                        _speed.value in 11.3..12.9 -> 12.0
                        _speed.value in 12.9..14.5 -> 13.0
                        _speed.value in 14.5..16.1 -> 14.7
                        _speed.value in 16.1..17.7 -> 16.4
                        _speed.value in 17.7..19.3 -> 19.2
                        else -> 20.0
                    }
                } else {
                    when {
                        _speed.value == 0.0 -> 0.0
                        _speed.value < 3.0 -> 4.5
                        _speed.value in 3.0..4.0 -> 6.2
                        _speed.value in 4.0..5.0 -> 8.5
                        _speed.value in 5.0..6.0 -> 10.0
                        _speed.value in 6.0..7.0 -> 11.2
                        _speed.value in 7.0..8.0 -> 12.0
                        _speed.value in 8.0..9.0 -> 13.0
                        _speed.value in 9.0..10.0 -> 14.7
                        _speed.value in 10.0..11.0 -> 16.4
                        _speed.value in 11.0..12.0 -> 19.2
                        else -> 20.0
                    }
                }

                val caloriesBurnedPerHour = MET * adjustedWeight

                val caloriesBurned: Double = if (durationInHours > 0) {
                    caloriesBurnedPerHour * durationInHours
                } else {
                    0.0
                }

                if (caloriesBurned > _caloriesBurned.value) {
                    _caloriesBurned.value = caloriesBurned
                }


                delay(100)
            } catch (ce: CancellationException) {
                println("calc CaloriesBurned runSessionRepo ${ce.message}")
            }
        }
    }

    suspend fun calcDuration() {
        ensureRepoScope()
        repoScope.launch {
            while (isActive) {
                try {
                    Log.d("RunSessionRepo", "calc duration")
                    val currentTime = DateTimeUtils.getCurrentInstant()
                    val currentDuration =
                        StatsUtils.calculateDuration(runSessionStartTime, currentTime)
                    totalDurationSeconds = cumulativeDurationSeconds + currentDuration
                    _duration.emit(totalDurationSeconds)

                    if (totalDurationSeconds > 2*60*60 && !durationNotification) {
                        notificationRepository.triggerNotification("BREAK")
                        durationNotification = true
                    }
                    delay(1000)
                } catch (ce: CancellationException) {
                    println("Cancel Duration update ${ce.message}")
                } catch (e: Exception) {
                    println(" calcDuration RunSessionRepo ${e.message}")
                }
            }
        }
    }

    suspend fun calcDistance() {
        ensureRepoScope()
        repoScope.launch {
            try {
                val latestLocationsFlow = gpsPointRepository.fetchTwoLatestLocation()

                latestLocationsFlow.collect { latestLocations ->
                    val (location1, location2) = latestLocations
                    Log.d("calc distance", "location 1: $location1, location 2: $location2")

                    val userUnitPreference = userInfo?.metricPreference

                    val distance = when (userUnitPreference) {
                        User.UNIT_MILE -> StatsUtils.haversineFormula(location1, location2) / 1609.34
                        else -> StatsUtils.haversineFormula(location1, location2) / 1000
                    }
                    Log.d("RunSessionRepo", "Computed: $distance")
                    if (distance <0.001 || distance >0.012 || (currentGPSPointId >= location1.gpsPointId && currentGPSPointId >= location2.gpsPointId)) {
                        return@collect
                    }
                    newDistance += distance
                    Log.d("RunSessionRepo", "New distance: $newDistance")
                    _distance.value = newDistance

                    currentGPSPointId = location2.gpsPointId
                    delay(100)
                }
            } catch (ce: CancellationException) {
                println("calcDistance runSessionRepo ${ce.message}")
            }
        }
    }

    suspend fun updateStatsSession(distance: Double, duration: Long, caloriesBurned: Double, pace: Double) {
        val currentSession = getCurrentSessionOrThrow()
        runSessionDao.updateStatsSession(currentSession.sessionId, distance, duration, caloriesBurned, pace)
    }
}