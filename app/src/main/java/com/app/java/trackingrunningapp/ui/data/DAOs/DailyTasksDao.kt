package com.app.java.trackingrunningapp.ui.data.DAOs

import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.app.java.trackingrunningapp.ui.data.entities.DailyTasks

interface DailyTasksDao {
    @Upsert
    suspend fun upsertDailyTask(dailyTasks: DailyTasks)

    @Delete
    suspend fun deleteDailyTask(dailyTasks: DailyTasks)

    @Query("UPDATE DailyTasks SET isShown = True WHERE taskId = :taskId")
    suspend fun showDailyTask(isShown: Boolean, taskId: Int)

    @Query("UPDATE DailyTasks SET isFinished = True WHERE taskId = :taskId")
    suspend fun finishDailyTask(isFinished: Boolean, taskId: Int)

    @Query("UPDATE DailyTasks SET isShown = False WHERE taskId = :taskId")
    suspend fun hideDailyTask(isShown: Boolean, taskId: Int)

}
