package com.app.java.trackingrunningapp.ui.data.DAOs

import androidx.room.Query
import androidx.room.Upsert
import com.app.java.trackingrunningapp.ui.data.entities.YearlyStats

interface YearlyStatsDao {
    @Query("SELECT * FROM YearlyStats WHERE yearlyStats = :year")
    suspend fun getYearlyStats(year: Int): YearlyStats?

    @Upsert
    suspend fun upsertYearlyStats(yearlyStats: YearlyStats)
}