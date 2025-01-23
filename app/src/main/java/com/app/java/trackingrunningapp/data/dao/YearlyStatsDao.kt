package com.app.java.trackingrunningapp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.app.java.trackingrunningapp.data.model.entity.YearlyStats

@Dao
interface YearlyStatsDao {
    @Upsert
    suspend fun upsertYearlyStats(yearlyStats: YearlyStats)
}