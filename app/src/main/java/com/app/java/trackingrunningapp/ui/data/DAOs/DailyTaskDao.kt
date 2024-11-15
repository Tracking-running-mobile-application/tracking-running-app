package com.app.java.trackingrunningapp.ui.data.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.app.java.trackingrunningapp.ui.data.entities.DailyTask

@Dao
interface DailyTaskDao {
    @Delete
    suspend fun deleteDailyTask(dailyTask: DailyTask)

    @Query("UPDATE DailyTask SET isShown = TRUE WHERE taskId = :taskId")
    suspend fun showDailyTask(isShown: Boolean, taskId: Int)

    @Query("UPDATE DailyTask SET isFinished = TRUE WHERE taskId = :taskId")
    suspend fun finishDailyTask(isFinished: Boolean, taskId: Int)

    @Query("UPDATE DailyTask SET isShown = FALSE WHERE taskId = :taskId")
    suspend fun hideDailyTask(isShown: Boolean, taskId: Int)

    @Query(
        """
        UPDATE DailyTask
        SET
            title = :title,
            description = :description,
            estimatedTime = :estimatedTime,
            exerciseType = :exerciseType,
            difficulty = :difficulty,
            lastRecommendedDate = :lastRecommendDate
        WHERE 
            taskId = :taskId
    """
    )
    suspend fun updatePartialDailyTask(taskId: Int, title: String, description: String, estimatedTime: Float, exerciseType: String, difficulty: String, lastRecommendDate: kotlinx.datetime.LocalDate)

    @Query("UPDATE DailyTask SET distanceProgress = :distanceProgress WHERE taskId = :taskId")
    suspend fun updateDistanceProgress(taskId: Int, distanceProgress: Float)
}
