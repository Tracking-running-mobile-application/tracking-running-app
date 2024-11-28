package com.app.java.trackingrunningapp.model.DAOs

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.app.java.trackingrunningapp.model.entities.Notification
import kotlinx.datetime.LocalDateTime

@Dao
interface NotificationDao {
    /***
     * TODO: Change partial update to upsert!
     * **/
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

    @Upsert
    suspend fun upsertNotification(notification: Notification)

}
