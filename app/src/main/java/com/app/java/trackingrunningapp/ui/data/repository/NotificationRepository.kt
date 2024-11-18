package com.app.java.trackingrunningapp.ui.data.repository

import com.app.java.trackingrunningapp.ui.data.DAOs.NotificationDao
import com.app.java.trackingrunningapp.ui.data.DAOs.RunSessionDao
import com.app.java.trackingrunningapp.ui.data.entities.Notification
import com.app.java.trackingrunningapp.ui.utils.DateTimeUtils
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import kotlinx.datetime.TimeZone.Companion.currentSystemDefault

class NotificationRepository(
    private val notificationDao: NotificationDao,
) {
    suspend fun createNotification (
        sessionId: Int?,
        title: String,
        message: String,
        notificationType: String,
    ) {
        /*null if not related to any sessions*/
        if (sessionId != null) {
            val currentTime = DateTimeUtils.getCurrentDateTime()
            val newNotification = Notification(
                notificationId = 0,
                sessionId = sessionId,
                title = title,
                message = message,
                notificationType = notificationType,
                timeTriggred = currentTime
            )
            notificationDao.upsertNotification(newNotification)
        } else {
            val currentTime = DateTimeUtils.getCurrentDateTime()
            notificationDao.partialUpdateNotification(
                notificationId = 0,
                sessionId = null,
                title = title,
                message = message,
                notificationType = notificationType,
                timeTriggered = currentTime
            )
        }
    }

    /* most likely not gonna need this since notification is just valuable right after being triggered
    suspend fun updateNotification(
        notificationId: Int,
        sessionId: Int?,
        title: String,
        message: String,
        notificationType: String,
    ) {
        val currentTime = DateTimeUtils.getCurrentDateTime()
        notificationDao.partialUpdateNotification(
            notificationId = notificationId,
            sessionId = sessionId,
            title = title,
            message = message,
            notificationType = notificationType,
            timeTriggered = currentTime
        )
    }
     */

    suspend fun deleteNotification(notificationId: Int) {
        notificationDao.deleteNotification(notificationId)
    }

    suspend fun getAllNotification() {
        notificationDao.getAllNotification()
    }
}