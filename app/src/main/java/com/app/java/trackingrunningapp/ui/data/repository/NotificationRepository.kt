package com.app.java.trackingrunningapp.ui.data.repository

import com.app.java.trackingrunningapp.ui.data.DAOs.NotificationDao
import com.app.java.trackingrunningapp.ui.data.DAOs.RunSessionDao
import com.app.java.trackingrunningapp.ui.data.entities.Notification
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import kotlinx.datetime.TimeZone.Companion.currentSystemDefault

class NotificationRepository(
    private val notificationDao: NotificationDao,
    private val runSessionDao: RunSessionDao
) {
    fun getCurrentDateTime(): LocalDateTime {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }
    suspend fun createNotification (
        sessionId: Int?,
        title: String,
        message: String,
        notificationType: String,
        timeTriggered: LocalDateTime = getCurrentDateTime()
    ) {
        /*null if not related to any sessions*/
        if (sessionId != null) {
            val newNotification = Notification(
                notificationId = 0,
                sessionId = sessionId,
                title = title,
                message = message,
                notificationType = notificationType,
                timeTriggred = timeTriggered
            )
            notificationDao.upsertNotification(newNotification)
        } else {
            notificationDao.partialUpdateNotification(
                notificationId = 0,
                sessionId = null,
                title = title,
                message = message,
                notificationType = notificationType,
                timeTriggered = timeTriggered
            )
        }
    }

    suspend fun updateNotification(
        notificationId: Int,
        sessionId: Int?,
        title: String,
        message: String,
        notificationType: String,
        timeTriggered: LocalDateTime = getCurrentDateTime()
    ) {
        notificationDao.partialUpdateNotification(
            notificationId = notificationId,
            sessionId = sessionId,
            title = title,
            message = message,
            notificationType = notificationType,
            timeTriggered = timeTriggered
        )
    }

    suspend fun deleteNotification(notificationId: Int) {
        notificationDao.deleteNotification(notificationId)
    }
}