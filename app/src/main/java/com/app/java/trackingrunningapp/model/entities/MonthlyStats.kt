package com.app.java.trackingrunningapp.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MonthlyStats(
    @PrimaryKey val monthStatsKey: String,
    @ColumnInfo(name = "totalDistance", defaultValue = "0.0") var totalDistance: Double? = 0.0,
    @ColumnInfo(name = "totalDuration", defaultValue = "0.0") var totalDuration: Long ? = 0L,
    @ColumnInfo(name = "totalCaloriesBurned", defaultValue = "0.0") var totalCaloriesBurned: Double ? = 0.0,
    @ColumnInfo(name = "totalAvgPace", defaultValue = "0.0") var totalAvgPace: Double ? = 0.0
)
