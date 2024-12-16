package com.app.java.trackingrunningapp.data.repository

import androidx.room.Delete
import com.app.java.trackingrunningapp.data.dao.UserDao
import com.app.java.trackingrunningapp.data.model.entity.user.User

class UserRepository(
    private val userDao: UserDao
) {
    suspend fun insertOneUser(user: User) {
         userDao.insertOneUser(user)
    }

    suspend fun updateUserInfo(user: User) {
         userDao.updateUserInfo(user)
    }

    @Delete
    suspend fun deleteUser(user: User) {
         userDao.deleteUser(user)
    }

    /**
     * Get user info
     */
    suspend fun getUserById(id: Int): User? {
        return userDao.getUserById(id)
    }
}
