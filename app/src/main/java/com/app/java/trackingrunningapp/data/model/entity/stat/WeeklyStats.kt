package com.app.java.trackingrunningapp.data.model.entity.stat

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("WeeklyStats")
data class WeeklyStats(
    @PrimaryKey
    @ColumnInfo("week_stats_key") val weeklyStatsKey: String,
    @ColumnInfo("total_distance") var totalDistance: Double? = 0.0,
    @ColumnInfo("total_duration") var totalDuration: Long? = 0L,
    @ColumnInfo("total_calories_burned") var totalCaloriesBurned: Double? = 0.0,
    @ColumnInfo(name = "total_avg_pace") var totalAvgPace: Double? = 0.0
)
