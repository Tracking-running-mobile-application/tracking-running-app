package com.app.java.trackingrunningapp.ui.data.DAOs

import androidx.room.Query
import androidx.room.Upsert
import com.app.java.trackingrunningapp.ui.data.entities.MonthlyStats

interface MonthlyStatsDao {
    @Query("SELECT * FROM MonthlyStats WHERE monthStatsKey = :month")
    suspend fun getMonthlyStats(month: String): MonthlyStats?

    @Upsert
    suspend fun upsertMonthlyStats(monthlyStats: MonthlyStats)
}