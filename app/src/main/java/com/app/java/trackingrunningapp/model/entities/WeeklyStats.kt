package com.app.java.trackingrunningapp.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeeklyStats(
    @PrimaryKey val weeklyStatsKey: String,
    var totalDistance: Double? = 0.0,
    var totalDuration: Long ? = 0L,
    var totalCaloriesBurned: Double ? = 0.0,
    var totalAvgPace: Double ? = 0.0
)
