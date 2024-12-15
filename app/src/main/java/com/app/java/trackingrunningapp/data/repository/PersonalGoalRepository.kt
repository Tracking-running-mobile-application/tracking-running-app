package com.app.java.trackingrunningapp.data.repository

import com.app.java.trackingrunningapp.data.dao.PersonalGoalDao
import com.app.java.trackingrunningapp.data.dao.RunSessionDao
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.model.entity.PersonalGoal
import com.app.java.trackingrunningapp.data.model.entity.RunSession
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

    private var updateJob: Job? = null

    private suspend fun getCurrentSessionOrThrow(): RunSession {
        val currentRunSession = runSessionDao.getCurrentRunSession()
        return currentRunSession ?: throw IllegalStateException("Value of current run session is null! (Personal Goal Repository)")
    }

    private suspend fun getCurrentPersonalGoalOrThrow(): PersonalGoal {
        val currentRunSession = getCurrentSessionOrThrow()
        return personalGoalDao.getPersonalGoalBySessionId(currentRunSession.sessionId)
            ?: throw IllegalStateException("There is not any personal goal attached with this run session ID! (Personal Goal Repository)")
    }

    suspend fun assignSessionToPersonalGoal() {
        val existingGoal = getCurrentPersonalGoalOrThrow()
        val currentSession = getCurrentSessionOrThrow()
        personalGoalDao.upsertPersonalGoal(existingGoal.copy(goalSessionId = currentSession.sessionId))
    }


    suspend fun upsertPersonalGoal(
        goalId: Int? = null,
        sessionId: Int? = null,
        targetDistance: Double?,
        targetDuration: Double?,
        targetCaloriesBurned: Double?,
        existingGoals: List<PersonalGoal>
    ): PersonalGoal {
        val existingGoal = goalId?.let {
            existingGoals.find { it.goalId == goalId }
        }

        val currentDateString = DateTimeUtils.getCurrentDate().toString()

        val personalGoal = existingGoal?.copy(
            goalSessionId = sessionId ?: existingGoal.goalSessionId,
            targetDistance = targetDistance ?: existingGoal.targetDistance,
            targetDuration = targetDuration ?: existingGoal.targetDuration,
            targetCaloriesBurned = targetCaloriesBurned ?: existingGoal.targetCaloriesBurned,
            dateCreated = currentDateString
        ) ?: PersonalGoal(
            goalId = 0,
            goalSessionId = sessionId,
            targetDistance = targetDistance,
            targetDuration = targetDuration,
            targetCaloriesBurned = targetCaloriesBurned,
            dateCreated = currentDateString
        )

        personalGoalDao.upsertPersonalGoal(personalGoal)
        return personalGoal
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

    private fun calcGoalProgress(runSession: RunSession, goal: PersonalGoal): Double {
        return when {
            goal.targetDistance != null -> goal.targetDistance?.let {
                (runSession.distance / it) * 100
            } ?: 0.0
            goal.targetDuration != null -> goal.targetDuration?.let {
                (runSession.duration / it) * 100
            } ?: 0.0
            goal.targetCaloriesBurned != null -> goal.targetCaloriesBurned?.let {
                (runSession.caloriesBurned / it) * 100
            } ?: 0.0
            else -> 0.0
        }
    }

    suspend fun updateGoalProgress() {
        val currentSession = getCurrentSessionOrThrow()
        val personalGoal = getCurrentPersonalGoalOrThrow()
        val progress = calcGoalProgress(currentSession, personalGoal)

        personalGoalDao.updateGoalProgress(personalGoal.goalId, progress)

        if (progress >= 100.0) {
            personalGoalDao.markGoalAchieved(personalGoal.goalId)
        }
    }

    suspend fun getAllPersonalGoals(): List<PersonalGoal> {
        return personalGoalDao.getAllPersonalGoals()
    }
}