package com.app.java.trackingrunningapp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.app.java.trackingrunningapp.data.model.entity.Notification

@Dao
interface NotificationDao {
    @Query("""
        SELECT * FROM Notification WHERE notificationType = :type ORDER BY RANDOM() LIMIT 1
    """)
    suspend fun getRandomNotificationByType(type: String): Notification

}
