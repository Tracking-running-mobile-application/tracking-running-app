package com.app.java.trackingrunningapp.model.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = RunSession::class,
            parentColumns = ["sessionId"],
            childColumns = ["goalSessionId"]
        )
    ],
    indices = [Index(value = ["goalSessionId"])]
)
data class PersonalGoal(
    @PrimaryKey(autoGenerate = true)
    val goalId: Int = 0,
    val goalSessionId : Int? = null,
    var targetDistance: Double?,
    var targetDuration: Double?,
    var targetCaloriesBurned: Double?,
    var goalProgress: Double = 0.0,
    var isAchieved: Boolean = false,
    var frequency: String,
    var dateCreated: String
)
