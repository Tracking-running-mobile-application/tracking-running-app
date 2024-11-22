package com.app.java.trackingrunningapp.ui.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MonthlyStats(
    @PrimaryKey val monthStatsKey: String,
    var totalDistance: Double? = 0.0,
    var totalDuration: Long? = 0L,
    var totalCaloriesBurned: Double? = 0.0,
    var totalAvgPace: Double? = 0.0
)
