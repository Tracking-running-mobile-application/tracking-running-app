package com.app.java.trackingrunningapp.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("TrainingPlan")
data class TrainingPlan(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("plan_id") val planId: Int = 0,
    @ColumnInfo("plan_session_id") val planSessionId: Int?,
    @ColumnInfo("plan_title") var title: String,
    @ColumnInfo("plan_description") var description: String,
    @ColumnInfo("plan_estimate_time") var estimatedTime: Double,
    @ColumnInfo("plan_target_distance") var targetDistance: Double? = 0.0,
    @ColumnInfo("plan_target_duration") var targetDuration: Double? = 0.0,
    @ColumnInfo("plan_target_calories_burned") var targetCaloriesBurned: Double? = 0.0,
    @ColumnInfo("plan_goal_progress") var goalProgress: Double? = 0.0,
    @ColumnInfo("plan_exercise_type") var exerciseType: String,
    @ColumnInfo("plan_image_path") var imagePath: String? = null,
    @ColumnInfo("plan_difficulty") var difficulty: String,
    @ColumnInfo("plan_last_recommended_date") var lastRecommendedDate: String? = null,
    @ColumnInfo("plan_is_finished") var isFinished: Boolean? = false
)
