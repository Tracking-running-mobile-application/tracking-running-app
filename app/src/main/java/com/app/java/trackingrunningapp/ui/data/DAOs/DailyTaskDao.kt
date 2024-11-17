package com.app.java.trackingrunningapp.ui.data.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.app.java.trackingrunningapp.ui.data.entities.DailyTask
import kotlinx.datetime.LocalDate

@Dao
interface DailyTaskDao {
    @Delete
    suspend fun deleteDailyTask(dailyTask: DailyTask)

    @Query("UPDATE DailyTask SET isShown = TRUE WHERE taskId = :taskId")
    suspend fun showDailyTask(taskId: Int)

    @Query("UPDATE DailyTask SET isFinished = TRUE WHERE taskId = :taskId")
    suspend fun finishDailyTask(taskId: Int)

    @Query("UPDATE DailyTask SET isShown = FALSE WHERE taskId = :taskId")
    suspend fun hideDailyTask(taskId: Int)

    @Query("UPDATE DailyTask SET isActive = TRUE WHERE taskId = :taskId")
    suspend fun activeDailyTask(taskId: Int)

    @Query("UPDATE DailyTask SET isActive = FALSE WHERE taskId = :taskId")
    suspend fun unactiveDailyTask(taskId: Int)

    @Query("SELECT * FROM DailyTask WHERE isActive = TRUE LIMIT 1")
    suspend fun getCurrentDailyTask() : DailyTask?


    @Query(
        """
        UPDATE DailyTask
        SET
            title = :title,
            description = :description,
            estimatedTime = :estimatedTime,
            exerciseType = :exerciseType,
            difficulty = :difficulty,
            lastRecommendedDate = :lastRecommendedDate
        WHERE 
            taskId = :taskId
    """
    )
    suspend fun updatePartialDailyTask(taskId: Int, title: String, description: String, estimatedTime: Float, exerciseType: String, difficulty: String, lastRecommendedDate: LocalDate)

    @Query("UPDATE DailyTask SET distanceProgress = :distanceProgress WHERE taskId = :taskId")
    suspend fun updateDistanceProgress(taskId: Int, distanceProgress: Float)
}
