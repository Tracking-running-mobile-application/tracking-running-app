package com.app.java.trackingrunningapp.ui.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
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
    /*currently not include sessionId as FK for the sake of simplicity*/
)
data class Notification(
    @PrimaryKey(autoGenerate = true)
    val notificationId : Int = 0,
    val sessionId: Int?,
    var title: String,
    var message: String,
    var timeTriggred: LocalDateTime,
    var notificationType: String
)
