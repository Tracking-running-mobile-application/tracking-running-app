package com.app.java.trackingrunningapp.model.repositories

import com.app.java.trackingrunningapp.model.DAOs.RunSessionDao
import com.app.java.trackingrunningapp.model.DAOs.UserDao
import com.app.java.trackingrunningapp.model.converters.LocalTimeConverter
import com.app.java.trackingrunningapp.model.entities.RunSession
import com.app.java.trackingrunningapp.model.entities.User
import com.app.java.trackingrunningapp.ui.utils.DateTimeUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RunSessionRepository(
    private val runSessionDao: RunSessionDao,
    userDao: UserDao
) {
    val convert = LocalTimeConverter()

    val userInfo = userDao.getUserInfo()

    private val _currentRunSession = MutableStateFlow<RunSession?>(null)
    val currentRunSession: StateFlow<RunSession?> = _currentRunSession

    private var offset: Int = 20
    private var limit: Int = 0


    private fun getCurrentSessionOrThrow(): RunSession {
        return currentRunSession.value ?: throw IllegalStateException("Value of current run session is null!")
    }

    private suspend fun initializeCurrentRunSession() {
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
        val currentSession = getCurrentSessionOrThrow()
        runSessionDao.startRunSession(currentSession.sessionId, runDate)

        initializeCurrentRunSession()
    }

    suspend fun finishRunSession() {
        val currentSession = getCurrentSessionOrThrow()
        runSessionDao.finishRunSession(currentSession.sessionId)

        initializeCurrentRunSession()
    }

    suspend fun addFavoriteRunSession(sessionId: Int) {
        val today = DateTimeUtils.getCurrentDate().toString()
        runSessionDao.addFavoriteRunSession(sessionId, today)
    }

    suspend fun removeFavoriteRunSession(sessionId: Int) {
        runSessionDao.removeFavoriteRunSession(sessionId)
    }

    suspend fun updatePaceRunSession(): Double {
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

        runSessionDao.updatePaceSession(
            sessionId = currentSession.sessionId,
            pace = pace
        )

        return pace
    }

    /*add in the viewmodel in the future to see run session detail*/
    suspend fun getRunSessionById(sessionId: Int): RunSession? {
        return runSessionDao.getRunSessionById(sessionId)
    }

    suspend fun updateCaloriesBurnedSession(): Double {
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

        runSessionDao.updateCaloriesBurnedSession(
            sessionId = currentSession.sessionId,
            caloriesBurned = caloriesBurned
        )

        return caloriesBurned
    }

    suspend fun updateDurationSession(duration: Long) {
        val currentSession = getCurrentSessionOrThrow()
        runSessionDao.updateDurationSession(currentSession.sessionId, duration)
    }

    suspend fun updateDistanceSession() {
    }

    suspend fun createMockData(runSession: RunSession) {
        runSessionDao.createMockDataForRunSession(runSession)
    }
}