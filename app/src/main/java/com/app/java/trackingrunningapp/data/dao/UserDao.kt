package com.app.java.trackingrunningapp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.app.java.trackingrunningapp.data.model.entity.User

@Dao
interface UserDao {
    @Query("SELECT * FROM User LIMIT 1")
    fun getUserInfo(): User?

    @Upsert
    suspend fun upsertUserInfo(user: User)
}