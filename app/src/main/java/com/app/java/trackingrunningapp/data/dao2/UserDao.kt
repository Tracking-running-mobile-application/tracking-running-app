package com.app.java.trackingrunningapp.data.dao2

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.app.java.trackingrunningapp.data.model.entity.user.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOneUser(user: User):Long
    @Update
    suspend fun updateUserInfo(user:User):Long
    @Delete
    suspend fun deleteUser(user: User):Long

    /**
     * Get user info
     */
    @Query("SELECT * FROM User WHERE user_id = :id")
    suspend fun getUserById(id:Int):User?

}