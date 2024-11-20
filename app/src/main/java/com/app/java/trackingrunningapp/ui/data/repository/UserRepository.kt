package com.app.java.trackingrunningapp.ui.data.repository

import com.app.java.trackingrunningapp.ui.data.DAOs.UserDao
import com.app.java.trackingrunningapp.ui.data.entities.User

class UserRepository(
    private val userDao: UserDao
) {
    suspend fun upsertUserInfo(user: User) {
        userDao.upsertUserInfo(user)
    }
}
