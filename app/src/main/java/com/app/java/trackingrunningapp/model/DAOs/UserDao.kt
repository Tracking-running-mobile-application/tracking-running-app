package com.app.java.trackingrunningapp.model.DAOs

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.app.java.trackingrunningapp.model.entities.User

@Dao
interface UserDao {
    @Query("SELECT * FROM User LIMIT 1")
    fun getUserInfo(): User?

    @Upsert
    suspend fun upsertUserInfo(user: User)
}
