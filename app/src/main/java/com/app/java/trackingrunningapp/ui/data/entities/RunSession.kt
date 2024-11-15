package com.app.java.trackingrunningapp.ui.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@Entity
data class RunSession(
    @PrimaryKey(autoGenerate = true)
    val sessionId: Int,
    var runDate: String,
    var startTime: LocalTime,
    var endTime: LocalTime,
    var distance: Float,
    var duration: Float,
    var pace: Float,
    var caloriesBurned: Float,
    var isActive: Boolean
)
