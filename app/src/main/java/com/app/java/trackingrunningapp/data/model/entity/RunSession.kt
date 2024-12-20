package com.app.java.trackingrunningapp.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RunSession(
    @PrimaryKey(autoGenerate = true)
    val sessionId: Int = 0,
    var runDate: String,
    @ColumnInfo(name = "distance", defaultValue = "0.0") var distance: Double? = 0.0,
    @ColumnInfo(name = "duration", defaultValue = "0") var duration: Long? = 0L,
    @ColumnInfo(name = "pace", defaultValue = "0.0") var pace: Double? = 0.0,
    @ColumnInfo(name = "caloriesBurned", defaultValue = "0.0") var caloriesBurned: Double? = 0.0,
    @ColumnInfo(name = "isActive", defaultValue = "0") var isActive: Boolean? = false,
    @ColumnInfo(name = "dateAddInFavorite", defaultValue = "NULL") var dateAddInFavorite: String? = null,
    @ColumnInfo(name = "isFavorite", defaultValue = "0") var isFavorite: Boolean = false
)
