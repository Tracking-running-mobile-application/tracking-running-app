package com.app.java.trackingrunningapp.ui.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = RunSession::class,
            parentColumns = ["sessionId"],
            childColumns = ["sessionId"]
        )
    ]
)
data class PersonalGoal(
    @PrimaryKey(autoGenerate = true)
    val goalId: Int = 0,
    val sessionId : Int,
    var targetDistance: Float?,
    var targetDuration: Float?,
    var targetCaloriesBurned: Float?,
    var goalProgress: Float = 0f,
    var isAchieved: Boolean = false,
    var frequency: String,
    var dateCreated: String
)
