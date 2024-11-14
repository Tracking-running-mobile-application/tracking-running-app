package com.app.java.trackingrunningapp.ui.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PersonalGoal(
    @PrimaryKey(autoGenerate = true)
    val goalId: Int = 0,
    val targetDistance: Float,
    val targetDuration: Float,
    val goalProgress: Float,
    val isAchieved: Boolean,
    val frequency: String
)
