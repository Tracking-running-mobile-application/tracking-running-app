package com.app.java.trackingrunningapp.data.model.entity.goal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("PersonalGoal")
data class PersonalGoal(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("goal_id") val goalId: Int = 0, // PK
    @ColumnInfo("goal_session_id") val goalSessionId : Int? = null, // FK
    @ColumnInfo("name") val name: String? = null,
    @ColumnInfo("target_distance") var targetDistance: Double? = 0.0,
    @ColumnInfo("target_duration") var targetDuration: Double? = 0.0,
    @ColumnInfo("target_calories_burned") var targetCaloriesBurned: Double? = 0.0,
    @ColumnInfo("goal_progress") var goalProgress: Double? = 0.0,
    @ColumnInfo("is_achieved") var isAchieved: Boolean = false,
    var dateCreated: String
)
