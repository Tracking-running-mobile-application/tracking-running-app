package com.app.java.trackingrunningapp.data.repository

import androidx.room.Query
import androidx.room.Update
import com.app.java.trackingrunningapp.data.dao2.RunSessionDao
import com.app.java.trackingrunningapp.data.dao2.TrainingPlanDao
import com.app.java.trackingrunningapp.data.model.entity.goal.RunSession
import com.app.java.trackingrunningapp.data.model.entity.TrainingPlan
import com.app.java.trackingrunningapp.utils.DateTimeUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus

class TrainingPlanRepository (
    private val trainingPlanDao: TrainingPlanDao
){
    suspend fun deleteTrainingPlan(planId: Int){
        trainingPlanDao.deleteTrainingPlan(planId)
    }
    suspend fun finishTrainingPlan(planId: Int){
        trainingPlanDao.finishTrainingPlan(planId)
    }
    suspend fun getGoalProgress(planId: Int): Double{
        return trainingPlanDao.getGoalProgress(planId)
    }
    suspend fun getTrainingPlanBySessionId(planSessionId: Int) : TrainingPlan?{
        return trainingPlanDao.getTrainingPlanBySessionId(planSessionId)
    }
    suspend fun getTrainingPlanByPlanId(planId: Int) : TrainingPlan?{
        return trainingPlanDao.getTrainingPlanByPlanId(planId)
    }
    suspend fun updateGoalProgress(planId: Int, goalProgress: Double){
        trainingPlanDao.updateGoalProgress(planId,goalProgress)
    }
    suspend fun updateTrainingPlan(trainingPlan: TrainingPlan){
        trainingPlanDao.updateTrainingPlan(trainingPlan)
    }
}