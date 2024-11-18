package com.app.java.trackingrunningapp.ui.data.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.app.java.trackingrunningapp.ui.data.entities.TrainingPlan
import kotlinx.datetime.LocalDate

@Dao
interface TrainingPlanDao {
    @Delete
    suspend fun deleteTrainingPlan(planId: Int)

    @Query("UPDATE TrainingPlan SET lastRecommendedDate = :lastRecommendDate WHERE planId = :planId")
    suspend fun showTrainingPlan(planId: Int, lastRecommendDate: LocalDate)

    @Query("UPDATE TrainingPlan SET isFinished = TRUE WHERE planId = :planId")
    suspend fun finishTrainingPlan(planId: Int)

    @Query("SELECT * FROM TrainingPlan WHERE sessionId = :sessionId LIMIT 1")
    suspend fun getTrainingPlanBySessionId(sessionId: Int) : TrainingPlan?

    @Query(
        """
        SELECT * FROM TrainingPlan
        WHERE lastRecommendedDate <= :dateLimit
        ORDER BY lastRecommendedDate ASC
        LIMIT :limit
    """)
    suspend fun getTrainingPlansNotShownSince(dateLimit: LocalDate, limit: Int): List<TrainingPlan>

    @Query(
        """
        UPDATE TrainingPlan
        SET
            title = :title,
            sessionId = :sessionId,
            description = :description,
            estimatedTime = :estimatedTime,
            targetDistance = :targetDistance,
            targetDuration = :targetDuration,
            targetCaloriesBurned = :targetCaloriesBurned,
            exerciseType = :exerciseType,
            difficulty = :difficulty
        WHERE 
            planId = :planId
    """
    )
    suspend fun updatePartialTrainingPlan(planId: Int, sessionId: Int, title: String, description: String, estimatedTime: Float, targetDistance: Float, targetDuration: Float, targetCaloriesBurned: Float, exerciseType: String, difficulty: String)

    @Query("UPDATE TrainingPlan SET goalProgress = :goalProgress WHERE planId = :planId")
    suspend fun updateGoalProgress(planId: Int, goalProgress: Float)
}