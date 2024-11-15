package com.app.java.trackingrunningapp.ui.data.DAOs

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.app.java.trackingrunningapp.ui.data.entities.WeeklyStats

@Dao
interface WeeklyStatsDao {
    @Query("SELECT * FROM WeeklyStats WHERE weeklyStats = :week")
    suspend fun getWeeklyStats(week: Int): WeeklyStats?

    @Upsert
    suspend fun upsertWeeklyStats(weeklyStats: WeeklyStats)
}