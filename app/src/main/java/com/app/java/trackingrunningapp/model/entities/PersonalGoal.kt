package com.app.java.trackingrunningapp.model.entities

import androidx.room.ColumnInfo
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
    @ColumnInfo(name = "targetDistance", defaultValue = "0.0") var targetDistance: Double? = 0.0,
    @ColumnInfo(name = "targetDuration", defaultValue = "0.0") var targetDuration: Double? = 0.0,
    @ColumnInfo(name = "targetCaloriesBurned", defaultValue = "0.0") var targetCaloriesBurned: Double? = 0.0,
    @ColumnInfo(name = "goalProgress", defaultValue = "0.0") var goalProgress: Double? = 0.0,
    var isAchieved: Boolean = false,
    var frequency: String,
    var dateCreated: String
)
