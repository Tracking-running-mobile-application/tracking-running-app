package com.app.java.trackingrunningapp.model.repositories

import com.app.java.trackingrunningapp.model.DAOs.PersonalGoalDao
import com.app.java.trackingrunningapp.model.DAOs.RunSessionDao
import com.app.java.trackingrunningapp.model.database.InitDatabase
import com.app.java.trackingrunningapp.model.entities.PersonalGoal
import com.app.java.trackingrunningapp.model.entities.RunSession
import com.app.java.trackingrunningapp.model.entities.TrainingPlan
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

    suspend fun activePersonalGoal(): Boolean {
        val currentRunSession = getCurrentSessionOrThrow()
        return personalGoalDao.getPersonalGoalBySessionId(currentRunSession.sessionId) != null
    }

    suspend fun upsertPersonalGoal(personalGoal: PersonalGoal) {
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

    /***
     * TODO:
     * make a fun to assign sessionID to the respective goal
     * **/

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

    private suspend fun updateGoalProgress() {
        val currentSession = getCurrentSessionOrThrow()

        val personalGoal = getCurrentPersonalGoalOrThrow()

        val progress = calcGoalProgress(currentSession, personalGoal)

        personalGoalDao.updateGoalProgress(personalGoal.goalId, progress)

        if (progress >= 100.0) {
            personalGoalDao.markGoalAchieved(personalGoal.goalId)
        }
    }

    suspend fun getAllPersonalGoals(): List<PersonalGoal> {
        val goals = personalGoalDao.getAllPersonalGoals()
        return goals.filter { !it.isAchieved }
    }
}