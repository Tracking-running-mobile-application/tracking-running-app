package com.app.java.trackingrunningapp.data.repository

import com.app.java.trackingrunningapp.data.dao2.NotificationDao
import com.app.java.trackingrunningapp.data.model.entity.notification.Notification
import com.app.java.trackingrunningapp.utils.DateTimeUtils

class NotificationRepository(
    private val notificationDao: NotificationDao
) {
    suspend fun partialUpdateNotification(
        notificationId: Int,
        title: String,
        message: String,
        notificationType: String
    ){
        notificationDao.partialUpdateNotification(notificationId,title,message,notificationType)
    }

    suspend fun updateNotification(notification: Notification){
        notificationDao.updateNotification(notification)
    }
}