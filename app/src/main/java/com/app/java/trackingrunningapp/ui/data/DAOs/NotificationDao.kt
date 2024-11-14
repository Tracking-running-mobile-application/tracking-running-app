package com.app.java.trackingrunningapp.ui.data.DAOs

import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.app.java.trackingrunningapp.ui.data.entities.Notification

interface NotificationDao {
    @Query("SELECT * FROM Notification WHERE sessionId = :sessionId")
    suspend fun getNotificationBySessionId(sessionId: Int): Notification

    @Upsert
    suspend fun upsertNotification(notification: Notification)

    @Delete
    suspend fun deleteNotification(notification: Notification)
}
