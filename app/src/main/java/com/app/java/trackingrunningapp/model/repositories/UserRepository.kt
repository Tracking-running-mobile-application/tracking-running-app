package com.app.java.trackingrunningapp.model.repositories

import com.app.java.trackingrunningapp.model.DAOs.UserDao
import com.app.java.trackingrunningapp.model.entities.User

class UserRepository(
    private val userDao: UserDao
) {
    suspend fun upsertUserInfo(user: User) {
        userDao.upsertUserInfo(user)
    }
}
