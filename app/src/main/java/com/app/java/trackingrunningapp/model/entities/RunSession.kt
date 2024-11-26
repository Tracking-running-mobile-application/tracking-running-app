package com.app.java.trackingrunningapp.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RunSession(
    @PrimaryKey(autoGenerate = true)
    val sessionId: Int,
    var runDate: String,
    var distance: Double,
    var duration: Long,
    var pace: Double,
    var caloriesBurned: Double,
    @ColumnInfo(name = "isActive", defaultValue = "0") var isActive: Boolean? = false,
    @ColumnInfo(name = "dateAddInFavorite", defaultValue = "NULL") var dateAddInFavorite: String? = null,
    @ColumnInfo(name = "isFavorite", defaultValue = "0") var isFavorite: Boolean = false
)
