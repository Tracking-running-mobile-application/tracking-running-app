package com.app.java.trackingrunningapp.data.model.entity

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
            childColumns = ["planSessionId"]
        )
    ],
    indices = [Index(value = ["planSessionId"])]
)
data class TrainingPlan(
    @PrimaryKey(autoGenerate = true)
    val planId: Int = 0,
    val planSessionId: Int?,
    var title: String,
    var description: String,
    var estimatedTime: Double,
    @ColumnInfo(name = "targetDistance", defaultValue = "0.0") var targetDistance: Double? = 0.0,
    @ColumnInfo(name = "targetDuration", defaultValue = "0.0") var targetDuration: Double? = 0.0,
    @ColumnInfo(name = "targetCaloriesBurned", defaultValue = "0.0") var targetCaloriesBurned: Double? = 0.0,
    @ColumnInfo(name = "goalProgress", defaultValue = "0.0") var goalProgress: Double? = 0.0,
    var difficulty: String,
)
