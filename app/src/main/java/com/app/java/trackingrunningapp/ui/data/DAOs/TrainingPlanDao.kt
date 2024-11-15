package com.app.java.trackingrunningapp.ui.data.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.app.java.trackingrunningapp.ui.data.entities.TrainingPlan

@Dao
interface TrainingPlanDao {
    @Delete
    suspend fun deleteTrainingPlan(trainingPlan: TrainingPlan)

    @Query("UPDATE TrainingPlan SET isShown = TRUE WHERE planId = :planId")
    suspend fun showTrainingPlan(planId: Int, isShown: Boolean)

    @Query("UPDATE TrainingPlan SET isFinished = TRUE WHERE planId = :planId")
    suspend fun finishTrainingPlan(isFinished: Boolean, planId: Int)

    @Query("UPDATE TrainingPlan SET isShown = FALSE WHERE planId = :planId")
    suspend fun hideTrainingPlan(isShown: Boolean, planId: Int)

    @Query(
        """
        UPDATE TrainingPlan
        SET
            title = :title,
            description = :description,
            estimatedTime = :estimatedTime,
            exerciseType = :exerciseType,
            difficulty = :difficulty,
            lastRecommendedDate = :lastRecommendDate
        WHERE 
            planId = :planId
    """
    )
    suspend fun updatePartialTrainingPlan(planId: Int, title: String, description: String, estimatedTime: Float, exerciseType: String, difficulty: String, lastRecommendDate: kotlinx.datetime.LocalDate)

    @Query("UPDATE TrainingPlan SET distanceProgress = :distanceProgress WHERE planId = :planId")
    suspend fun updateDistanceProgress(planId: Int, distanceProgress: Float)
}