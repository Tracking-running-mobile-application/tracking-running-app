package com.app.java.trackingrunningapp.model.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime

@Entity
data class Notification(
    @PrimaryKey(autoGenerate = true)
    val notificationId : Int = 0,
    var title: String,
    var message: String,
    var notificationType: String
)
