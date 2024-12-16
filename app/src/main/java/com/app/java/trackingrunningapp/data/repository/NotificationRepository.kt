package com.app.java.trackingrunningapp.data.repository

import com.app.java.trackingrunningapp.data.dao.NotificationDao
import com.app.java.trackingrunningapp.data.model.entity.notification.Notification

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