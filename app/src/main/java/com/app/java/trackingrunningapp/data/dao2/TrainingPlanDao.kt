package com.app.java.trackingrunningapp.data.dao2

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.app.java.trackingrunningapp.data.model.entity.TrainingPlan

@Dao
interface TrainingPlanDao {

    @Delete
    suspend fun deleteTrainingPlan(planId: Int)

    @Query("UPDATE TrainingPlan SET plan_is_finished = TRUE WHERE plan_id = :planId")
    suspend fun finishTrainingPlan(planId: Int)

    @Query("SELECT plan_goal_progress FROM TrainingPlan WHERE plan_id = :planId")
    suspend fun getGoalProgress(planId: Int): Double

    @Query("SELECT * FROM TrainingPlan WHERE plan_session_id = :planSessionId LIMIT 1")
    suspend fun getTrainingPlanBySessionId(planSessionId: Int) : TrainingPlan?

    @Query("SELECT * FROM TrainingPlan WHERE plan_id = :planId LIMIT 1")
    suspend fun getTrainingPlanByPlanId(planId: Int) : TrainingPlan?

    @Query(
        """
        SELECT * FROM TrainingPlan
        WHERE plan_last_recommended_date <= :dateLimit 
        AND plan_is_finished = False
        ORDER BY plan_last_recommended_date ASC
        LIMIT :limit
    """)
    suspend fun getTrainingPlansNotShownSince(dateLimit: String, limit: Int): List<TrainingPlan>


    @Query("UPDATE TrainingPlan SET plan_goal_progress = :goalProgress WHERE plan_id = :planId")
    suspend fun updateGoalProgress(planId: Int, goalProgress: Double)

    @Update
    suspend fun updateTrainingPlan(trainingPlan: TrainingPlan)
}