package com.app.java.trackingrunningapp.ui.data.repository

import com.app.java.trackingrunningapp.ui.data.DAOs.RunSessionDao
import com.app.java.trackingrunningapp.ui.data.DAOs.UserDao
import com.app.java.trackingrunningapp.ui.data.converters.LocalTimeConverter
import com.app.java.trackingrunningapp.ui.data.entities.RunSession
import com.app.java.trackingrunningapp.ui.data.entities.User
import com.app.java.trackingrunningapp.ui.utils.DateTimeUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

class RunSessionRepository(
    private val runSessionDao: RunSessionDao,
    private val userDao: UserDao
) {
    val convert = LocalTimeConverter()

    suspend fun filterRunningSessionByDay(startDate: String, endDate: String): Flow<List<RunSession>> {
        return runSessionDao.filterRunningSessionByDay(startDate, endDate)
    }

    suspend fun getAllRunSessions(): Flow<List<RunSession>> {
         return runSessionDao.getAllRunSessions()
    }

    suspend fun deleteRunSession(sessionId: Int) {
         runSessionDao.deleteRunSession(sessionId)
    }

    suspend fun startRunSession(sessionId: Int) {
        val startTime = DateTimeUtils.getCurrentTime()
        val runDate = convert.fromLocalDate(DateTimeUtils.getCurrentDate())

        runSessionDao.startRunSession(sessionId, startTime, runDate)
    }

    /*maybe merge pause with this?*/
    suspend fun finishRunSession(sessionId: Int) {
        val endTime = DateTimeUtils.getCurrentTime()

         runSessionDao.finishRunSession(sessionId, endTime)
    }

    suspend fun updateRunSession(distance: Float, duration: Float, caloriesBurned: Float) {
        /*very likely we gonna take the gps by every few seconds, thus the val*/
        val durationInMinutes = duration / 60

        val userInfo = userDao.getUserInfo()
        val userUnitPreference = userInfo?.unit

        val adjustedDistance = when (userUnitPreference) {
            User.UNIT_MILE -> distance * 0.621371f
            else -> distance
        }

        val pace = if (adjustedDistance > 0) {
            durationInMinutes / adjustedDistance
        } else {
            0f
        }

        val currentRunSession = runSessionDao.getCurrentRunSession()

        if (currentRunSession != null) {
            runSessionDao.updateStatsSession(
                sessionId = currentRunSession.sessionId,
                distance = adjustedDistance,
                duration = duration,
                caloriesBurned = caloriesBurned,
                pace = pace
            )
        } else {
            println("No current run session found!");
        }
    }

    suspend fun getCurrentRunSession(): RunSession? {
        return runSessionDao.getCurrentRunSession()
    }

    /*add in the viewmodel in the future to see run session detail*/
    suspend fun getRunSessionById(sessionId: Int): RunSession? {
        return runSessionDao.getRunSessionById(sessionId)
    }
}