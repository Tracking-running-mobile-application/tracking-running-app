package com.app.java.trackingrunningapp.ui.data.DAOs

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.app.java.trackingrunningapp.ui.data.entities.Notification
import kotlinx.datetime.LocalDateTime

@Dao
interface NotificationDao {
    @Query("SELECT * FROM Notification WHERE notificationId = :notificationId")
    suspend fun getNotificationByNotificationId(notificationId: Int): Notification?

    @Query("SELECT * FROM Notification WHERE notiRunSessionId = :notiRunSessionId")
    suspend fun getNotificationBySessionId(notiRunSessionId: Int): Notification?

    @Query("""
        UPDATE Notification
        SET 
            notiRunSessionId = :notiRunSessionId,
            title = :title,
            message = :message,
            notificationType = :notificationType,
            timeTriggred = :timeTriggered
        WHERE 
            notificationId = :notificationId
    """)
    suspend fun partialUpdateNotification(notificationId: Int, notiRunSessionId: Int?, title: String, message: String, notificationType: String, timeTriggered: LocalDateTime)

    @Query("DELETE FROM notification WHERE notificationId = :notificationId")
    suspend fun deleteNotification(notificationId: Int)

    @Upsert
    suspend fun upsertNotification(notification: Notification)

    @Query("SELECT * FROM Notification ORDER BY notificationId DESC")
    suspend fun getAllNotification(): List<Notification>
}
