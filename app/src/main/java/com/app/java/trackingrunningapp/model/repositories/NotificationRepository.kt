package com.app.java.trackingrunningapp.model.repositories

import android.content.Context
import com.app.java.trackingrunningapp.model.DAOs.NotificationDao
import com.app.java.trackingrunningapp.model.database.InitDatabase
import com.app.java.trackingrunningapp.model.entities.Notification
import com.app.java.trackingrunningapp.modelbase.RunningDatabase
import com.app.java.trackingrunningapp.ui.utils.DateTimeUtils

class NotificationRepository(
    context: Context
) {
    val db = InitDatabase.runningDatabase

    private val notificationDao: NotificationDao = db.notificationDao()

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
                notiRunSessionId = sessionId,
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
                notiRunSessionId = null,
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