package com.app.java.trackingrunningapp.ui.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MonthlyStats(
    @PrimaryKey val monthStatsKey: String,
    var totalDistance: Float = 0f,
    var totalDuration: Float = 0f,
    var totalCaloriesBurned: Float = 0f,
    var totalPace: Float = 0f
)
