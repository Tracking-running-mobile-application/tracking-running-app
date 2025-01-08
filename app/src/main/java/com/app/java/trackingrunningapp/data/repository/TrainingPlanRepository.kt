package com.app.java.trackingrunningapp.data.repository

import com.app.java.trackingrunningapp.data.dao.RunSessionDao
import com.app.java.trackingrunningapp.data.dao.TrainingPlanDao
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.database.InitDatabase.Companion.notificationRepository
import com.app.java.trackingrunningapp.data.model.entity.RunSession
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
    private val runSessionDao: RunSessionDao = db.runSessionDao()

    private var updateJob: Job? = null

    private suspend fun getCurrentSessionOrThrow(): RunSession {
        val currentRunSession = runSessionDao.getCurrentRunSession()
        return currentRunSession ?: throw IllegalStateException("There is not any current run session ! (Training Plan Repository)")
    }

    private suspend fun getCurrentTrainingPlanOrThrow(): TrainingPlan {
        val currentRunSession = getCurrentSessionOrThrow()
        return trainingPlanDao.getTrainingPlanBySessionId(currentRunSession.sessionId)
            ?: throw IllegalStateException("There is not any training plan attached with this run session ID! (Training Plan Repository)")
    }

    suspend fun activeTrainingPlan(): Boolean {
        val currentRunSession = getCurrentSessionOrThrow()
        return trainingPlanDao.getTrainingPlanBySessionId(currentRunSession.sessionId) != null
    }

    suspend fun getTrainingPlanByDifficulty(exerciseType: String): List<TrainingPlan> {
        return trainingPlanDao.getTrainingPlansByDifficulty(exerciseType)
    }

    suspend fun getGoalProgress(): Double {
        val currentTrainingPlan = getCurrentTrainingPlanOrThrow()
        return trainingPlanDao.getGoalProgress(currentTrainingPlan.planId)
    }

    suspend fun assignSessionToTrainingPlan() {
        val existingPlan = getCurrentTrainingPlanOrThrow()
        val currentSession = getCurrentSessionOrThrow()
        trainingPlanDao.upsertTrainingPlan(existingPlan.copy(planSessionId = currentSession.sessionId))
    }

    suspend fun assignLastDateToTrainingPlan(planId: Int, lastRecommendedDate: String) {
        val existingPlan = trainingPlanDao.getTrainingPlanByPlanId(planId)
        if (existingPlan != null) {
            val updatedPlan = existingPlan.copy(lastRecommendedDate = lastRecommendedDate)
            trainingPlanDao.upsertTrainingPlan(updatedPlan)
        } else {
            println("The plan with this id doesn't exist!")
        }
    }


    suspend fun deleteTrainingPlan(planId: Int) {
        trainingPlanDao.deleteTrainingPlan(planId)
    }

    private fun calcGoalProgress(runSession: RunSession, plan: TrainingPlan): Double {
        return when {
            plan.targetDistance != null -> plan.targetDistance?.let {
                (runSession.distance?.div(it))?.times(100)
            } ?: 0.0
            plan.targetDuration != null -> plan.targetDuration?.let {
                (runSession.duration?.div(it))?.times(100)
            } ?: 0.0
            plan.targetCaloriesBurned != null -> plan.targetCaloriesBurned?.let {
                (runSession.caloriesBurned?.div(it))?.times(100)
            } ?: 0.0
            else -> 0.0
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
        val currentSession = getCurrentSessionOrThrow()
        val currentTrainingPlan = getCurrentTrainingPlanOrThrow()
        val progress = calcGoalProgress(currentSession, currentTrainingPlan)

        trainingPlanDao.updateGoalProgress(currentTrainingPlan.planId, progress)

        if (progress >= 50.0) {
            notificationRepository.triggerNotification("HALF")
        }

        if ( progress >= 100.0 ) {
            trainingPlanDao.finishTrainingPlan(currentTrainingPlan.planId)
            notificationRepository.triggerNotification("COMPLETE")
            stopUpdatingGoalProgress()
        }
    }
}