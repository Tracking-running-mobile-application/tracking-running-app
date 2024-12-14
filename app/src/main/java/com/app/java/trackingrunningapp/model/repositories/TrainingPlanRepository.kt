package com.app.java.trackingrunningapp.model.repositories

import com.app.java.trackingrunningapp.model.DAOs.RunSessionDao
import com.app.java.trackingrunningapp.model.DAOs.TrainingPlanDao
import com.app.java.trackingrunningapp.model.database.InitDatabase
import com.app.java.trackingrunningapp.model.entities.RunSession
import com.app.java.trackingrunningapp.model.entities.TrainingPlan
import com.app.java.trackingrunningapp.ui.utils.DateTimeUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus

class TrainingPlanRepository {
    val db = InitDatabase.runningDatabase

    private val trainingPlanDao: TrainingPlanDao = db.trainingPlanDao()
    private val runSessionDao: RunSessionDao = db.runSessionDao()

    private var lastFetchDate = DateTimeUtils.getCurrentDate()

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

    suspend fun updateTrainingPlanRecommendation(limit: Int = 4): List<TrainingPlan> {
        val today = DateTimeUtils.getCurrentDate()
        val dateLimit = today.minus(7, DateTimeUnit.DAY).toString()

        if (lastFetchDate < today) {
           lastFetchDate = today

           return trainingPlanDao.getTrainingPlansNotShownSince(dateLimit, limit)
        }

        return emptyList()
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

        if ( progress >= 100.0 ) {
            trainingPlanDao.finishTrainingPlan(currentTrainingPlan.planId)
            /*noti after finish?*/
        }
    }
}