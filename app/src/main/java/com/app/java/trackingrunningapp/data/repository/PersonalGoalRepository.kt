package com.app.java.trackingrunningapp.data.repository

import android.app.Application
import android.content.Context
import android.util.Log
import com.app.java.trackingrunningapp.data.dao.PersonalGoalDao
import com.app.java.trackingrunningapp.data.dao.RunSessionDao
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.model.entity.PersonalGoal
import com.app.java.trackingrunningapp.data.model.entity.RunSession
import com.app.java.trackingrunningapp.model.repositories.NotificationRepository
import com.app.java.trackingrunningapp.utils.DateTimeUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

class PersonalGoalRepository {
    val db = InitDatabase.runningDatabase

    private val personalGoalDao: PersonalGoalDao = db.personalGoalDao()
    private val runSessionDao: RunSessionDao = db.runSessionDao()

    private val notificationRepository: NotificationRepository = InitDatabase.notificationRepository
    private val runSessionRepository: RunSessionRepository = InitDatabase.runSessionRepository
    private var updateJob: Job? = null

    private var halfNotiTriggered: Boolean = false
    private var finishNotiTriggered: Boolean = false

    private suspend fun getCurrentSessionOrThrow(): RunSession {
        val currentRunSession = runSessionDao.getCurrentRunSession()
        return currentRunSession ?: throw IllegalStateException("Value of current run session is null! (Personal Goal Repository)")
    }

    private suspend fun getCurrentPersonalGoalOrThrow(): PersonalGoal {
        return runSessionRepository.currentRunSession.value?.let {
            personalGoalDao.getPersonalGoalBySessionId(
                it.sessionId)
        }
            ?: throw IllegalStateException("There is not any personal goal attached with this run session ID! (Personal Goal Repository)")
    }

    suspend fun assignSessionToPersonalGoal(goalId: Int) {
        Log.d("PersonalGoal", "5")
        val currentSessionId = runSessionRepository.currentRunSession.value?.sessionId
        if (currentSessionId != null) {
            personalGoalDao.setSessionForPersonalGoal(goalId = goalId, sessionId = currentSessionId)
        }
        halfNotiTriggered = false
        finishNotiTriggered = false
    }

    suspend fun upsertPersonalGoal(
        goalId: Int? = null,
        sessionId: Int? = null,
        name: String? = null,
        targetDistance: Double?,
        targetDuration: Double?,
        targetCaloriesBurned: Double?,
    ) {
        val currentDateString = DateTimeUtils.getCurrentDate().toString()

        val personalGoal = PersonalGoal(
            goalId = goalId?: 0,
            name = name,
            goalSessionId = sessionId,
            targetDistance = targetDistance,
            targetDuration = targetDuration,
            targetCaloriesBurned = targetCaloriesBurned,
            dateCreated = currentDateString
        )

        personalGoalDao.upsertPersonalGoal(personalGoal)
    }


    suspend fun deletePersonalGoal(goalId: Int) {
        personalGoalDao.deletePersonalGoal(goalId)
    }

    suspend fun getGoalProgress(): Double {
        val currentPersonalGoal = getCurrentPersonalGoalOrThrow()
        return personalGoalDao.getGoalProgress(currentPersonalGoal.goalId)
    }

    suspend fun startUpdatingGoalProgress() {
        updateJob?.cancel()
        updateJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
               try {
                   updateGoalProgress()
                   delay(5000)
               } catch (e: Exception) {
                   println("Error updating goal progress: ${e.message}")
               }
            }
        }
    }

    fun stopUpdatingGoalProgress() {
        if (updateJob?.isActive == true) {
            updateJob?.cancel()
            updateJob = null
        }
    }

    private suspend fun calcGoalProgress(goal: PersonalGoal): Double {
        val stats = runSessionRepository.fetchStatsSession()
        return when {
            goal.targetDistance != null -> goal.targetDistance?.let {
                (stats.distance?.div(it))?.times(100)
            } ?: 0.0
            goal.targetDuration != null -> goal.targetDuration?.let {
                (stats.duration?.div(it))?.times(100)
            } ?: 0.0
            goal.targetCaloriesBurned != null -> goal.targetCaloriesBurned?.let {
                (stats.caloriesBurned?.div(it))?.times(100)
            } ?: 0.0
            else -> 0.0
        }
    }

    suspend fun updateGoalProgress() {
        val personalGoal = getCurrentPersonalGoalOrThrow()
        val progress = calcGoalProgress(personalGoal)

        personalGoalDao.updateGoalProgress(personalGoal.goalId, progress)

        if (progress >= 50 && !halfNotiTriggered) {
            notificationRepository.triggerNotification("HALF")
            halfNotiTriggered = true
        }

        if (progress >= 100.0 && !finishNotiTriggered) {
            notificationRepository.triggerNotification("COMPLETE")
            stopUpdatingGoalProgress()
            finishNotiTriggered = true
        }
    }

    suspend fun getAllPersonalGoals(): List<PersonalGoal> {
        return personalGoalDao.getAllPersonalGoals()
    }
}