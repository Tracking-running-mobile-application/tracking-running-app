package com.app.java.trackingrunningapp.model.repositories

import com.app.java.trackingrunningapp.model.DAOs.UserDao
import com.app.java.trackingrunningapp.model.database.InitDatabase
import com.app.java.trackingrunningapp.model.entities.User
import com.app.java.trackingrunningapp.modelbase.RunningDatabase

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
