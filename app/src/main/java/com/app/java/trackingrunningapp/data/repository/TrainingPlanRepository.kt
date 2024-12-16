package com.app.java.trackingrunningapp.data.repository

import com.app.java.trackingrunningapp.data.dao.TrainingPlanDao
import com.app.java.trackingrunningapp.data.model.entity.TrainingPlan

class TrainingPlanRepository (
    private val trainingPlanDao: TrainingPlanDao
){
    suspend fun deleteTrainingPlan(plan: TrainingPlan){
        trainingPlanDao.deleteTrainingPlan(plan)
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