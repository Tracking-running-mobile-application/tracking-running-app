package com.app.java.trackingrunningapp.model.repositories

import android.content.Context
import com.app.java.trackingrunningapp.model.DAOs.UserDao
import com.app.java.trackingrunningapp.model.entities.User
import com.app.java.trackingrunningapp.modelbase.RunningDatabase

class UserRepository(
    context: Context
) {
    val db = RunningDatabase.getInstance(context)

    private val userDao: UserDao = db.userDao()

    suspend fun upsertUserInfo(user: User) {
        userDao.upsertUserInfo(user)
    }
}
