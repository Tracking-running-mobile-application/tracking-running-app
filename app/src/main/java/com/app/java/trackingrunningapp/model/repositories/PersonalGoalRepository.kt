package com.app.java.trackingrunningapp.model.repositories

import com.app.java.trackingrunningapp.model.DAOs.PersonalGoalDao
import com.app.java.trackingrunningapp.model.entities.PersonalGoal
import com.app.java.trackingrunningapp.model.entities.RunSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

class PersonalGoalRepository(
    private val personalGoalDao: PersonalGoalDao,
    private val runSessionRepository: RunSessionRepository
) {
    private val currentRunSession = runSessionRepository.currentRunSession

    private var updateJob: Job? = null

    private fun getCurrentSessionOrThrow(): RunSession {
        return currentRunSession.value ?: throw IllegalStateException("Value of current run session is null!")
    }

    suspend fun upsertPersonalGoal(personalGoal: PersonalGoal) {
        personalGoalDao.upsertPersonalGoal(personalGoal)
    }

    suspend fun deletePersonalGoal(goalId: Int) {
        personalGoalDao.deletePersonalGoal(goalId)
    }

    suspend fun startPersonalGoal() {
        updateJob?.cancel()
        updateJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
               try {
                   val currentRunSession = runSessionRepository.currentRunSession.value
                   if (currentRunSession != null) {
                       updateGoalProgress()
                   } else {
                       println("No active session found, stopping goal updates.")
                       stopPersonalGoal()
                       break
                   }
                   delay(5000)
               } catch (e: Exception) {
                   println("Error updating goal progress: ${e.message}")
               }
            }
        }
    }

    fun stopPersonalGoal() {
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

        val personalGoal = personalGoalDao.getPersonalGoalBySessionId(currentSession.sessionId)

        if (personalGoal != null) {
            val progress = calcGoalProgress(currentSession, personalGoal)

            personalGoalDao.updateGoalProgress(personalGoal.goalId, progress)

            if (progress >= 100.0) {
                personalGoalDao.markGoalAchieved(personalGoal.goalId)
            }
        } else {
            println("No personal goal linked to the current run session!")
        }
    }

    suspend fun getAllPersonalGoals(): List<PersonalGoal> {
        val goals = personalGoalDao.getAllPersonalGoals()
        return goals.filter { !it.isAchieved }
    }
}