package com.app.java.trackingrunningapp.ui.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class DailyTask(
    @PrimaryKey(autoGenerate = true)
    val taskId: Int = 0,
    var title: String,
    var description: String,
    var estimatedTime: Float,
    var targetDistance: Float,
    var distanceProgress: Float,
    var exerciseType: String,
    var difficulty: String,
    var lastRecommendedDate: LocalDate,
    var isShown: Boolean,
    var isActive: Boolean,
    var isFinished: Boolean
)
