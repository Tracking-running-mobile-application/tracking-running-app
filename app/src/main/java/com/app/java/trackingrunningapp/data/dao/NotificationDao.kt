package com.app.java.trackingrunningapp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.app.java.trackingrunningapp.data.model.entity.Notification

@Dao
interface NotificationDao {
    /***
     * TODO: Change partial update to upsert!
     * **/
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

    @Upsert
    suspend fun upsertNotification(notification: Notification)

}
