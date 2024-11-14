package com.app.java.trackingrunningapp.ui.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalTime

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = RunSession::class,
            parentColumns = ["sessionId"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Notification(
    @PrimaryKey(autoGenerate = true)
    val notificationId : Int? = 0,
    val sessionId: Int?,
    var title: String,
    var message: String,
    var timeTriggred: LocalTime,
    var notificationType: String
)
