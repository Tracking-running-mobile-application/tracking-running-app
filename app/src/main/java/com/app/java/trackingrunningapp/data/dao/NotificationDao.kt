package com.app.java.trackingrunningapp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.app.java.trackingrunningapp.data.model.entity.notification.Notification

@Dao
interface NotificationDao {
    /***
     * TODO: Change partial update to upsert!
     * **/
    @Query("""
        UPDATE Notification
        SET 
            notification_title = :title,
            notification_message = :message,
            notification_type = :notificationType
        WHERE 
            notification_id = :notificationId
    """)
    suspend fun partialUpdateNotification(
        notificationId: Int, title: String, message: String, notificationType: String
    )

    @Update
    suspend fun updateNotification(notification: Notification)

}
