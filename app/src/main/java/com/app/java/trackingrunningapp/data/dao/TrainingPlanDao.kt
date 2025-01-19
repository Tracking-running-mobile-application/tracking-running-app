package com.app.java.trackingrunningapp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.app.java.trackingrunningapp.data.model.entity.TrainingPlan

@Dao
interface TrainingPlanDao {
    @Query("SELECT goalProgress FROM TrainingPlan WHERE planId = :planId")
    suspend fun getGoalProgress(planId: Int): Double

    @Query("SELECT * FROM TrainingPlan WHERE planSessionId = :planSessionId LIMIT 1")
    suspend fun getTrainingPlanBySessionId(planSessionId: Int) : TrainingPlan?

    @Query("SELECT * FROM TrainingPlan WHERE planId = :planId LIMIT 1")
    suspend fun getTrainingPlanByPlanId(planId: Int) : TrainingPlan?

    @Query(
        """
        SELECT * FROM TrainingPlan
        WHERE difficulty =:difficulty 
    """)
    suspend fun getTrainingPlansByDifficulty(difficulty: String): List<TrainingPlan>


    @Query("UPDATE TrainingPlan SET goalProgress = :goalProgress WHERE planId = :planId")
    suspend fun updateGoalProgress(planId: Int, goalProgress: Double)

    @Query("UPDATE TrainingPlan SET planSessionId = :sessionId WHERE planId = :planId")
    suspend fun assignSessionToPlan(planId: Int, sessionId: Int)
}