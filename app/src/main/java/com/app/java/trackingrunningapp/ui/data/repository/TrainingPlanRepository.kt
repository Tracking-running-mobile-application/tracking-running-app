package com.app.java.trackingrunningapp.ui.data.repository

import com.app.java.trackingrunningapp.ui.data.DAOs.RunSessionDao
import com.app.java.trackingrunningapp.ui.data.DAOs.TrainingPlanDao
import com.app.java.trackingrunningapp.ui.data.converters.LocalTimeConverter
import com.app.java.trackingrunningapp.ui.data.entities.TrainingPlan
import com.app.java.trackingrunningapp.ui.utils.DateTimeUtils
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus

class TrainingPlanRepository(
    private val trainingPlanDao: TrainingPlanDao,
    private val runSessionDao: RunSessionDao
) {
    private var lastFetchDate = DateTimeUtils.getCurrentDate()

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
        estimatedTime: Float,
        targetDistance: Float,
        targetDuration: Float,
        targetCaloriesBurned: Float,
        exerciseType: String,
        difficulty: String
    ) {
        val currentRunSession = runSessionDao.getCurrentRunSession()

        if (currentRunSession != null) {
            trainingPlanDao.updatePartialTrainingPlan(
                planId = 0,
                sessionId = currentRunSession.sessionId,
                title = title,
                description = description,
                estimatedTime = estimatedTime,
                targetDistance = targetDistance,
                targetDuration = targetDuration,
                targetCaloriesBurned = targetCaloriesBurned,
                exerciseType = exerciseType,
                difficulty = difficulty
            )
        } else {
            println("No run session found to be connected to this goal yet!");
        }
    }

    /*add func to calc progress*/

    suspend fun deleteTrainingPlan(planId: Int) {
        trainingPlanDao.deleteTrainingPlan(planId)
    }
}