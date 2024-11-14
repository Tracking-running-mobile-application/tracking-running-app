package com.app.java.trackingrunningapp.ui.data.DAOs

import androidx.room.Query
import androidx.room.Upsert
import com.app.java.trackingrunningapp.ui.data.entities.User
import kotlinx.coroutines.flow.Flow

interface UserDao {
    @Query("SELECT * FROM user")
    fun getUserInfo(): Flow<List<User>>

    @Upsert
    suspend fun upsertUserInfo(user: User)
}
