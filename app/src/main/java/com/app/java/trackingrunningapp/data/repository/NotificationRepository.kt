package com.app.java.trackingrunningapp.model.repositories

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.data.dao.NotificationDao
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.model.entity.Notification
import kotlinx.coroutines.launch

class NotificationRepository {
    val db = InitDatabase.runningDatabase

    private val notificationDao: NotificationDao = db.notificationDao()

    suspend fun triggerNotification(type: String, context: Context) {
        val notification = notificationDao.getRandomNotificationByType(type)

        sendNotification(notification, context)
    }

    private fun sendNotification(notification: Notification, context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            "goal_channel_id",
            "Goal Notifications",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)


        val notificationBuilder = NotificationCompat.Builder(context, "goal_channel_id")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(notification.title)
            .setContentText(notification.message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        notificationManager.notify(notification.notificationId, notificationBuilder.build())
    }

}