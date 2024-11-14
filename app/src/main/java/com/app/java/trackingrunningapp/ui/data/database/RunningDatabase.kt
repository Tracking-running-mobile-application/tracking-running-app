package com.app.java.trackingrunningapp.ui.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.java.trackingrunningapp.ui.data.converters.LocalTimeConverter
import com.app.java.trackingrunningapp.ui.data.entities.MonthlyStats
import com.app.java.trackingrunningapp.ui.data.DAOs.MonthlyStatsDao
import com.app.java.trackingrunningapp.ui.data.entities.PersonalGoal
import com.app.java.trackingrunningapp.ui.data.DAOs.PersonalGoalDao
import com.app.java.trackingrunningapp.ui.data.DAOs.RunSessionDao
import com.app.java.trackingrunningapp.ui.data.entities.User
import com.app.java.trackingrunningapp.ui.data.DAOs.UserDao
import com.app.java.trackingrunningapp.ui.data.entities.WeeklyStats
import com.app.java.trackingrunningapp.ui.data.DAOs.WeeklyStatsDao
import com.app.java.trackingrunningapp.ui.data.entities.YearlyStats
import com.app.java.trackingrunningapp.ui.data.DAOs.YearlyStatsDao
import com.app.java.trackingrunningapp.ui.data.entities.RunSession

@Database (
    entities = [User::class, PersonalGoal::class, WeeklyStats::class, MonthlyStats::class, YearlyStats::class, RunSession::class],
    version = 1
)
@TypeConverters(LocalTimeConverter::class)
abstract class RunningDatabase: RoomDatabase() {
    abstract val userDao: UserDao
    abstract val personalGoalDao: PersonalGoalDao
    abstract val weeklyStatsDao: WeeklyStatsDao
    abstract val monthlyStatsDao: MonthlyStatsDao
    abstract val yearlyStatsDao: YearlyStatsDao
    abstract val runSessionDao: RunSessionDao
}