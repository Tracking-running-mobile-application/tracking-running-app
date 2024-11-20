package com.app.java.trackingrunningapp.ui.data.repository

import com.app.java.trackingrunningapp.ui.data.DAOs.RunSessionDao
import com.app.java.trackingrunningapp.ui.data.DAOs.TrainingPlanDao
import com.app.java.trackingrunningapp.ui.data.converters.LocalTimeConverter
import com.app.java.trackingrunningapp.ui.data.entities.PersonalGoal
import com.app.java.trackingrunningapp.ui.data.entities.RunSession
import com.app.java.trackingrunningapp.ui.data.entities.TrainingPlan
import com.app.java.trackingrunningapp.ui.utils.DateTimeUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus

class TrainingPlanRepository(
    private val trainingPlanDao: TrainingPlanDao,
    private val runSessionRepository: RunSessionRepository
) {
    private var lastFetchDate = DateTimeUtils.getCurrentDate()

    private var updateJob: Job? = null

    private val currentRunSession = runSessionRepository.currentRunSession

    private fun getCurrentSessionOrThrow(): RunSession {
        return currentRunSession.value ?: throw IllegalStateException("Value of current run session is null!")
    }

    suspend fun updateTrainingPlanRecommendation(limit: Int = 4): List<TrainingPlan> {
        val today = DateTimeUtils.getCurrentDate()

        if (today.minus(1, DateTimeUnit.DAY) == lastFetchDate) {
           lastFetchDate = today
           return trainingPlanDao.getTrainingPlansNotShownSince(today.minus(7, DateTimeUnit.DAY), limit)
        }

        return emptyList()
    }

    suspend fun createTrainingPlan(
        title: String,
        description: String,
        estimatedTime: Double,
        targetDistance: Double?,
        targetDuration: Double?,
        targetCaloriesBurned: Double?,
        exerciseType: String,
        difficulty: String
    ) {
        val currentSession = getCurrentSessionOrThrow()

        trainingPlanDao.updatePartialTrainingPlan(
            planId = 0,
            sessionId = currentSession.sessionId,
            title = title,
            description = description,
            estimatedTime = estimatedTime,
            targetDistance = targetDistance ?: 0.0,
            targetDuration = targetDuration ?: 0.0,
            targetCaloriesBurned = targetCaloriesBurned ?: 0.0,
            exerciseType = exerciseType,
            difficulty = difficulty
        )
    }

    suspend fun deleteTrainingPlan(planId: Int) {
        trainingPlanDao.deleteTrainingPlan(planId)
    }

    private fun calcGoalProgress(runSession: RunSession, plan: TrainingPlan): Double {
        return when {
            plan.targetDistance != null -> plan.targetDistance?.let {
                (runSession.distance / it) * 100
            } ?: 0.0
            plan.targetDuration != null -> plan.targetDuration?.let {
                (runSession.duration / it) * 100
            } ?: 0.0
            plan.targetCaloriesBurned != null -> plan.targetCaloriesBurned?.let {
                (runSession.caloriesBurned / it) * 100
            } ?: 0.0
            else -> 0.0
        }
    }

    fun startTrainingPlan(sessionId: Int) {
        updateJob?.cancel()
        updateJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                try {
                    val currentRunSession = runSessionRepository.currentRunSession.value
                    if (currentRunSession != null) {
                        updateGoalProgress()
                    } else {
                        println("No active session found, stopping goal updates.")
                        stopTrainingPlan()
                        break
                    }
                    delay(5000)
                } catch (e: Exception) {
                    println("Error updating goal progress: ${e.message}")
                }
            }
        }
    }

    fun stopTrainingPlan() {
        if (updateJob?.isActive == true) {
            updateJob?.cancel()
            updateJob = null
        }
    }

    suspend fun updateGoalProgress() {
        val currentSession = getCurrentSessionOrThrow()

        val currentTrainingPlan = trainingPlanDao.getTrainingPlanBySessionId(currentSession.sessionId)

            if (currentTrainingPlan != null) {
                val progress = calcGoalProgress(currentSession, currentTrainingPlan)

                trainingPlanDao.updateGoalProgress(currentTrainingPlan.planId, progress)

                if ( progress >= 100.0 ) {
                    trainingPlanDao.finishTrainingPlan(currentTrainingPlan.planId)
                }

            } else {
                println("No training plan linked to the current run session!")
            }
    }

    suspend fun getTrainingPlanBySessionId(sessionId: Int): TrainingPlan? {
        return trainingPlanDao.getTrainingPlanBySessionId(sessionId)
    }

}