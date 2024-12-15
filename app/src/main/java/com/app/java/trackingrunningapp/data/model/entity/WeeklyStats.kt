package com.app.java.trackingrunningapp.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeeklyStats(
    @PrimaryKey val weeklyStatsKey: String,
    @ColumnInfo(name = "totalDistance", defaultValue = "0.0") var totalDistance: Double? = 0.0,
    @ColumnInfo(name = "totalDuration", defaultValue = "0") var totalDuration: Long ? = 0L,
    @ColumnInfo(name = "totalCaloriesBurned", defaultValue = "0.0") var totalCaloriesBurned: Double ? = 0.0,
    @ColumnInfo(name = "totalAvgPace", defaultValue = "0.0") var totalAvgPace: Double ? = 0.0
)
