package com.app.java.trackingrunningapp.ui.data.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.app.java.trackingrunningapp.ui.data.entities.Notification
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

@Dao
interface NotificationDao {
    @Query("SELECT * FROM Notification WHERE notificationId = :notificationId")
    suspend fun getNotificationByNotificationId(notificationId: Int): Notification?

    @Query("SELECT * FROM Notification WHERE sessionId = :sessionId")
    suspend fun getNotificationBySessionId(sessionId: Int): Notification?

    @Query("""
        UPDATE Notification
        SET 
            sessionId = :sessionId,
            title = :title,
            message = :message,
            notificationType = :notificationType,
            timeTriggred = :timeTriggered
        WHERE 
            notificationId = :notificationId
    """)
    suspend fun partialUpdateNotification(notificationId: Int, sessionId: Int?, title: String, message: String, notificationType: String, timeTriggered: LocalDateTime)

    @Delete
    suspend fun deleteNotification(notificationId: Int)

    @Upsert
    suspend fun upsertNotification(notification: Notification)

    @Query("SELECT * FROM Notification WHERE notificationType = :notificationType")
    suspend fun getNotificationByType(notificationType: String): Notification?
}
