package com.app.java.trackingrunningapp.data.repository

import com.app.java.trackingrunningapp.data.dao2.RunSessionDao
import com.app.java.trackingrunningapp.data.dao2.UserDao
import com.app.java.trackingrunningapp.utils.LocalTimeConverter
import com.app.java.trackingrunningapp.data.model.entity.goal.RunSession
import com.app.java.trackingrunningapp.data.model.entity.user.User
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
    private val runSessionDao: RunSessionDao
) {
    suspend fun deleteRunSession(sessionId: Int): Long {
        return runSessionDao.deleteRunSession(sessionId)
    }

    suspend fun getAllRunSessions(limit: Int, offset: Int): List<RunSession> {
        return runSessionDao.getAllRunSessions(limit, offset)
    }

    suspend fun getAllFavoriteRunSessions(): List<RunSession> {
        return runSessionDao.getAllFavoriteRunSessions()
    }

    suspend fun getCurrentRunSession(): RunSession? {
        return runSessionDao.getCurrentRunSession()
    }

    suspend fun getRunSessionById(sessionId: Int): RunSession? {
        return runSessionDao.getRunSessionById(sessionId)
    }

    suspend fun updateCaloriesBurnedSession(sessionId: Int, caloriesBurned: Double) {
        return runSessionDao.updateCaloriesBurnedSession(sessionId, caloriesBurned)
    }

    suspend fun updatePaceSession(sessionId: Int, pace: Double) {
        runSessionDao.updatePaceSession(sessionId, pace)
    }

    suspend fun updateDurationSession(sessionId: Int, duration: Long) {
        runSessionDao.updateDurationSession(sessionId, duration)
    }

    suspend fun updateDistanceSession(sessionId: Int, distance: Double) {
        runSessionDao.updateDistanceSession(sessionId, distance)
    }

    suspend fun updateStatsSession(
        sessionId: Int, distance: Double, duration: Long, caloriesBurned: Double, pace: Double
    ) {
        runSessionDao.updateStatsSession(sessionId, distance, duration, caloriesBurned, pace)
    }

    suspend fun initiateRunSession(sessionId: Int, runDate: String){
        runSessionDao.initiateRunSession(sessionId,runDate)
    }
    suspend fun setRunSessionInactive(sessionId: Int){
        runSessionDao.setRunSessionInactive(sessionId)
    }
    suspend fun addFavoriteRunSession(sessionId: Int, dateAddInFavorite: String){
        runSessionDao.addFavoriteRunSession(sessionId,dateAddInFavorite)
    }
    suspend fun removeFavoriteRunSession(sessionId: Int){
        runSessionDao.removeFavoriteRunSession(sessionId)
    }
    suspend fun filterRunningSessionByDay(startDate: String, endDate: String): List<RunSession>{
        return runSessionDao.filterRunningSessionByDay(startDate,endDate)
    }

    suspend fun fetchStatsSession(sessionId: Int): StatsSession{
        return runSessionDao.fetchStatsSession(sessionId)
    }
//    suspend fun getCompleteSessionData(sessionId: Int): List<CompleteSessionData>{
//        return runSessionDao.getCompleteSessionData(sessionId)
//    }


}