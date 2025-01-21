package com.app.java.trackingrunningapp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.app.java.trackingrunningapp.data.model.entity.WeeklyStats

@Dao
interface WeeklyStatsDao {
    @Upsert
    suspend fun upsertWeeklyStats(weeklyStats: WeeklyStats)
}