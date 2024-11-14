package com.app.java.trackingrunningapp.ui.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int? = 0,
    val name: String?,
    val age: Int?,
    val height: Float?,
    val weight: Float?
)
