package com.app.java.trackingrunningapp.ui.data.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.app.java.trackingrunningapp.ui.data.entities.Notification
import kotlinx.datetime.LocalTime

@Dao
interface NotificationDao {
    @Query("SELECT * FROM Notification WHERE sessionId = :sessionId")
    suspend fun getNotificationBySessionId(sessionId: Int): Notification

    @Query("""
        UPDATE Notification
        SET 
            title = :title,
            message = :message,
            notificationType = :notificationType
        WHERE 
            notificationId = :notificationId
    """)
    suspend fun partialUpdateNotification(notificationId: Int, title: String, message: String, notificationType: String)

    @Delete
    suspend fun deleteNotification(notification: Notification)

    @Query("UPDATE Notification SET timeTriggred = :timeTriggered WHERE notificationId = :notificationId")
    suspend fun setTriggeredTimeByNotificationId(notificationId: Int, timeTriggered: LocalTime)
}
