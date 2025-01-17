package com.app.java.trackingrunningapp.data.repository

import com.app.java.trackingrunningapp.data.dao.TrainingPlanDao
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.database.InitDatabase.Companion.notificationRepository
import com.app.java.trackingrunningapp.data.model.entity.TrainingPlan
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TrainingPlanRepository {
    val db = InitDatabase.runningDatabase

    private val trainingPlanDao: TrainingPlanDao = db.trainingPlanDao()
    private val runSessionRepository: RunSessionRepository = RunSessionRepository()

    private var updateJob: Job? = null

    private var halfNotiTriggered: Boolean = false
    private var finishNotiTriggered: Boolean = false

    private suspend fun getCurrentTrainingPlanOrThrow(): TrainingPlan {
        val currentRunSessionId = runSessionRepository.currentRunSession.value?.sessionId
        return currentRunSessionId?.let { trainingPlanDao.getTrainingPlanBySessionId(it) }
            ?: throw IllegalStateException("There is not any training plan attached with this run session ID! (Training Plan Repository)")
    }

    suspend fun getTrainingPlanByDifficulty(exerciseType: String): List<TrainingPlan> {
        return trainingPlanDao.getTrainingPlansByDifficulty(exerciseType)
    }

    suspend fun getGoalProgress(): Double {
        val currentTrainingPlan = getCurrentTrainingPlanOrThrow()
        return trainingPlanDao.getGoalProgress(currentTrainingPlan.planId)
    }

    suspend fun assignSessionToTrainingPlan(planId: Int) {
        val currentSessionId = runSessionRepository.currentRunSession.value?.sessionId
        if (currentSessionId != null) {
            trainingPlanDao.assignSessionToPlan(planId = planId, sessionId = currentSessionId)
        }
        halfNotiTriggered = false
        finishNotiTriggered = false
    }

    suspend fun deleteTrainingPlan(planId: Int) {
        trainingPlanDao.deleteTrainingPlan(planId)
    }

    private fun calcGoalProgress(plan: TrainingPlan): Double {
        return when {
            plan.targetDistance != null && plan.targetDistance!! > 0 -> {
                val distance = runSessionRepository.distance.value
                if (distance > 0) {
                    (distance / plan.targetDistance!!) * 100
                } else {
                    0.0
                }
            }

            plan.targetDuration != null && plan.targetDuration!! > 0 -> {
                val durationInSeconds = runSessionRepository.duration.value
                if (durationInSeconds > 0) {
                    (durationInSeconds / (plan.targetDuration!! * 60)) * 100
                } else {
                    0.0
                }
            }

            plan.targetCaloriesBurned != null && plan.targetCaloriesBurned!! > 0 -> {
                val caloriesBurned = runSessionRepository.caloriesBurned.value
                if (caloriesBurned > 0) {
                    (caloriesBurned / plan.targetCaloriesBurned!!) * 100
                } else {
                    0.0
                }
            }
            else -> -1.0
        }
    }

    fun startUpdatingGoalProgress() {
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

    suspend fun updateGoalProgress() {
        val currentTrainingPlan = getCurrentTrainingPlanOrThrow()
        val progress = calcGoalProgress(currentTrainingPlan)

        trainingPlanDao.updateGoalProgress(currentTrainingPlan.planId, progress)

        if (progress >= 50 && !halfNotiTriggered) {
            notificationRepository.triggerNotification("HALF")
            halfNotiTriggered = true
        }

        if ( progress >= 100.0 && !finishNotiTriggered) {
            notificationRepository.triggerNotification("COMPLETE")
            finishNotiTriggered = true
            stopUpdatingGoalProgress()
        }
    }
}