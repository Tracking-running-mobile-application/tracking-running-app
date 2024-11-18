package com.app.java.trackingrunningapp.ui.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = RunSession::class,
            parentColumns = ["sessionId"],
            childColumns = ["sessionId"]
        )
    ]
)
data class TrainingPlan(
    @PrimaryKey(autoGenerate = true)
    val planId: Int = 0,
    val sessionId: Int,
    var title: String,
    var description: String,
    var estimatedTime: Float,
    var targetDistance: Float?,
    var targetDuration: Float?,
    var targetCaloriesBurned: Float?,
    var goalProgress: Float,
    var exerciseType: String,
    var difficulty: String,
    var lastRecommendedDate: LocalDate,
    var isFinished: Boolean = false
)
