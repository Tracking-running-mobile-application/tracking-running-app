package com.app.java.trackingrunningapp

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.java.trackingrunningapp.data.dao.GPSPointDao
import com.app.java.trackingrunningapp.data.dao.GPSTrackDao
import com.app.java.trackingrunningapp.data.dao.MonthlyStatsDao
import com.app.java.trackingrunningapp.data.dao.NotificationDao
import com.app.java.trackingrunningapp.data.dao.PersonalGoalDao
import com.app.java.trackingrunningapp.data.dao.RunSessionDao
import com.app.java.trackingrunningapp.data.dao.TrainingPlanDao
import com.app.java.trackingrunningapp.data.dao.UserDao
import com.app.java.trackingrunningapp.data.dao.WeeklyStatsDao
import com.app.java.trackingrunningapp.data.dao.YearlyStatsDao
import com.app.java.trackingrunningapp.data.database.RunningDatabase
import com.app.java.trackingrunningapp.data.model.entity.User
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserEntityTest {

    private lateinit var appDatabase: RunningDatabase
    private lateinit var userDao: UserDao
    private lateinit var weeklyStatsDao: WeeklyStatsDao
    private lateinit var monthlyStatsDao: MonthlyStatsDao
    private lateinit var yearlyStatsDao: YearlyStatsDao
    private lateinit var notificationDao: NotificationDao
    private lateinit var gpsPointDao: GPSPointDao
    private lateinit var gpsTrackDao: GPSTrackDao
    private lateinit var trainingPlanDao: TrainingPlanDao
    private lateinit var personalGoalDao: PersonalGoalDao
    private lateinit var runSessionDao: RunSessionDao

    @Before
    fun setUp() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RunningDatabase::class.java
        ).allowMainThreadQueries().build()

        userDao = appDatabase.userDao()
        weeklyStatsDao = appDatabase.weeklyStatsDao()
        monthlyStatsDao = appDatabase.monthlyStatsDao()
        yearlyStatsDao = appDatabase.yearlyStatsDao()
        notificationDao = appDatabase.notificationDao()
        gpsPointDao = appDatabase.GPSPointDao()
        gpsTrackDao = appDatabase.GPSTrackDao()
        trainingPlanDao = appDatabase.trainingPlanDao()
        personalGoalDao = appDatabase.personalGoalDao()
        runSessionDao = appDatabase.runSessionDao()
    }

    @After
    fun closeDatabase() {
        appDatabase.close()
    }

    @Test
    fun testDefaultValueUser() = runBlocking {
        val user = User(name = "NULL", age = 23, height = 32.0f)
        userDao.upsertUserInfo(user)

        val retrievedUser = userDao.getUserInfo()
        assertEquals("kg", retrievedUser?.unit)
        assertEquals("km", retrievedUser?.metricPreference)

    }

    @Test
    fun testUpsertAndGetUserInfo() = runBlocking {
        val user = User(name = "Nguyen Vu", age = 20, height = 167.0f, weight = 75.0, metricPreference = "kg", unit = "km")
        userDao.upsertUserInfo(user)

        val retrievedUser = userDao.getUserInfo()
        assertNotNull(retrievedUser)
        assertEquals("Nguyen Vu", retrievedUser?.name)
        assertEquals(75.0, retrievedUser?.weight)
        assertNotEquals("Nguyen Vu", retrievedUser?.unit)
    }

}