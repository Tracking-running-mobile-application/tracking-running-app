package com.app.java.trackingrunningapp.data.repository

import com.app.java.trackingrunningapp.data.dao.UserDao
import com.app.java.trackingrunningapp.data.model.entity.User

class UserRepository {
    val db = InitDatabase.runningDatabase

    private val userDao: UserDao = db.userDao()

    suspend fun upsertUserInfo(user: User) {
        userDao.upsertUserInfo(user)
    }

    suspend fun getUserInfo(): User? {
        return userDao.getUserInfo()
    }
}
