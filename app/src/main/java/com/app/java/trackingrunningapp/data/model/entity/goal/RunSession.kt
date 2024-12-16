package com.app.java.trackingrunningapp.data.model.entity.goal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RunSession(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("session_id") val sessionId: Int,
    @ColumnInfo("run_date") var runDate: String,
    @ColumnInfo("speed") var speed: Double,
    @ColumnInfo("distance") var distance: Double,
    @ColumnInfo("duration") var duration: Long,
    @ColumnInfo("pace") var pace: Double,
    @ColumnInfo("calories_burned") var caloriesBurned: Double,
    @ColumnInfo("is_active") var isActive: Boolean = false,
    @ColumnInfo("date_add_in_favorite") var dateAddInFavorite: String? = null,
    @ColumnInfo("is_favorite") var isFavorite: Boolean = false
)
