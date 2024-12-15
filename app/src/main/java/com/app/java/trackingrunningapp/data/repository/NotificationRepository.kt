package com.app.java.trackingrunningapp.data.repository

import com.app.java.trackingrunningapp.data.dao.NotificationDao
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.model.entity.Notification
import com.app.java.trackingrunningapp.utils.DateTimeUtils

class NotificationRepository {
    val db = InitDatabase.runningDatabase

    private val notificationDao: NotificationDao = db.notificationDao()

    /*Change this to upsert*/
    suspend fun createNotification (
        sessionId: Int?,
        title: String,
        message: String,
        notificationType: String,
    ) {
        /*null if not related to any sessions*/
        if (sessionId != null) {
            val newNotification = Notification(
                notificationId = 0,
                title = title,
                message = message,
                notificationType = notificationType,
            )
            notificationDao.upsertNotification(newNotification)
        } else {
            val currentTime = DateTimeUtils.getCurrentDateTime()
            notificationDao.partialUpdateNotification(
                notificationId = 0,
                title = title,
                message = message,
                notificationType = notificationType
            )
        }
    }
}