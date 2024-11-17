package com.app.java.trackingrunningapp.ui.data.repository

import com.app.java.trackingrunningapp.ui.data.DAOs.RunSessionDao
import com.app.java.trackingrunningapp.ui.data.DAOs.TrainingPlanDao

class TrainingPlanRepository(
    private val trainingPlanDao: TrainingPlanDao,
    private val runSessionDao: RunSessionDao
) {
    suspend fun calcDistanceProgress(planId: Int): Float {
        val currentRunSession = runSessionDao.getCurrentRunSession()
        val currentTrainingPlan = trainingPlanDao.getCurrentTrainingPlan()
        val distanceCovered = currentRunSession?.distance ?: 0f
        val targetDistance = currentTrainingPlan?.targetDistance ?: 0f
        var distanceProgress = currentTrainingPlan?.distanceProgress ?: 0f

        if (distanceCovered > 0f) {
            distanceProgress = (distanceCovered / targetDistance) * 100
        } else {
            println("Warning: This run is not being attached to any run session yet!")
        }

        trainingPlanDao.updateDistanceProgress(planId, distanceProgress)

        return distanceProgress
    }

    suspend fun finishTrainingPlan(planId: Int) {
        trainingPlanDao.finishTrainingPlan(planId)
    }

    suspend fun showTrainingPlan(planId: Int) {
        trainingPlanDao.showTrainingPlan(planId)
    }

    suspend fun hideTrainingPlan(planId: Int) {
        trainingPlanDao.hideTrainingPlan(planId)
    }

    suspend fun startTrainingPlan(planId: Int) {
        trainingPlanDao.activeTrainingPlan(planId)
    }

    suspend fun stopTrainingPlan(planId: Int) {
        trainingPlanDao.unactiveTrainingPlan(planId)
    }
}