package com.app.java.trackingrunningapp.data.model.entity.notification

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Notification(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("notification_id") val notificationId : Int = 0, // PK
    @ColumnInfo("notification_run_session_id") val notifySessionId:Int, // FK Run Session
    @ColumnInfo("notification_title")var title: String,
    @ColumnInfo("notification_message")var message: String,
    @ColumnInfo("notification_type")var notificationType: String
)
