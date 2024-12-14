package com.app.java.trackingrunningapp.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Notification(
    @PrimaryKey(autoGenerate = true)
    val notificationId : Int = 0,
    var title: String,
    var message: String,
    var notificationType: String
)
