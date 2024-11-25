package com.app.java.trackingrunningapp.model.DAOs

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.app.java.trackingrunningapp.model.entities.YearlyStats

@Dao
interface YearlyStatsDao {
    @Query("SELECT * FROM YearlyStats WHERE yearlyStatsKey = :year")
    suspend fun getYearlyStats(year: String): YearlyStats?

    @Upsert
    suspend fun upsertYearlyStats(yearlyStats: YearlyStats)
}