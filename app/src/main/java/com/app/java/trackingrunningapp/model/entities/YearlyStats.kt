package com.app.java.trackingrunningapp.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class YearlyStats(
    @PrimaryKey val yearlyStatsKey: String,
    var totalDistance: Double? = 0.0,
    var totalDuration: Long? = 0L,
    var totalCaloriesBurned: Double? = 0.0,
    var totalAvgPace: Double? = 0.0,
)
