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
    var distance: Double,
    var duration: Long,
    var pace: Double,
    var caloriesBurned: Double,
    var isActive: Boolean,
    var dateAddInFavorite: String? = null,
    var isFavorite: Boolean = false
)
