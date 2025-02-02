package com.app.java.trackingrunningapp.data.repository

import android.util.Log
import com.app.java.trackingrunningapp.data.dao.UserDao
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.model.entity.User

class UserRepository {
    val db = InitDatabase.runningDatabase

    private val userDao: UserDao = db.userDao()

    suspend fun upsertUserInfo(user: User) {
        userDao.upsertUserInfo(user)
    }

    fun getUserInfo(): User? {
        return userDao.getUserInfo()
        Log.d("User Repository", "get user info")
    }
}
