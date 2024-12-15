package com.app.java.trackingrunningapp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.app.java.trackingrunningapp.data.model.entity.TrainingPlan

@Dao
interface TrainingPlanDao {
    @Query("DELETE FROM TrainingPlan WHERE planId = :planId")
    suspend fun deleteTrainingPlan(planId: Int)

    @Query("UPDATE TrainingPlan SET isFinished = TRUE WHERE planId = :planId")
    suspend fun finishTrainingPlan(planId: Int)

    @Query("SELECT goalProgress FROM TrainingPlan WHERE planId = :planId")
    suspend fun getGoalProgress(planId: Int): Double

    @Query("SELECT * FROM TrainingPlan WHERE planSessionId = :planSessionId LIMIT 1")
    suspend fun getTrainingPlanBySessionId(planSessionId: Int) : TrainingPlan?

    @Query("SELECT * FROM TrainingPlan WHERE planId = :planId LIMIT 1")
    suspend fun getTrainingPlanByPlanId(planId: Int) : TrainingPlan?

    @Query(
        """
        SELECT * FROM TrainingPlan
        WHERE lastRecommendedDate <= :dateLimit 
        AND isFinished = False
        ORDER BY lastRecommendedDate ASC
        LIMIT :limit
    """)
    suspend fun getTrainingPlansNotShownSince(dateLimit: String, limit: Int): List<TrainingPlan>


    @Query("UPDATE TrainingPlan SET goalProgress = :goalProgress WHERE planId = :planId")
    suspend fun updateGoalProgress(planId: Int, goalProgress: Double)

    @Upsert
    suspend fun upsertTrainingPlan(trainingPlan: TrainingPlan)
}