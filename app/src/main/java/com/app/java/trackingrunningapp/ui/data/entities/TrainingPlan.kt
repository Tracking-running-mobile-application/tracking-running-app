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
    var estimatedTime: Double,
    var targetDistance: Double?,
    var targetDuration: Double?,
    var targetCaloriesBurned: Double?,
    var goalProgress: Double,
    var exerciseType: String,
    var difficulty: String,
    var lastRecommendedDate: LocalDate,
    var isFinished: Boolean = false
)
