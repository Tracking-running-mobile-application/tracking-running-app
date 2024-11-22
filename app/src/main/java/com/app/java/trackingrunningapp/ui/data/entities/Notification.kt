package com.app.java.trackingrunningapp.ui.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = RunSession::class,
            parentColumns = ["sessionId"],
            childColumns = ["notiRunSessionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["notiRunSessionId"])]
    /*include fk of goalId in the future to send a noti when progress reach 100% */
)
data class Notification(
    @PrimaryKey(autoGenerate = true)
    val notificationId : Int = 0,
    val notiRunSessionId: Int?,
    var title: String,
    var message: String,
    var timeTriggred: LocalDateTime,
    var notificationType: String
)
