package com.app.java.trackingrunningapp.data.model.entity

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
