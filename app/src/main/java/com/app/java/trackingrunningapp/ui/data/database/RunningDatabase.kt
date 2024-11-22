package com.app.java.trackingrunningapp.ui.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.java.trackingrunningapp.ui.data.DAOs.GPSPointDao
import com.app.java.trackingrunningapp.ui.data.DAOs.GPSTrackDao
import com.app.java.trackingrunningapp.ui.data.converters.LocalTimeConverter
import com.app.java.trackingrunningapp.ui.data.entities.MonthlyStats
import com.app.java.trackingrunningapp.ui.data.DAOs.MonthlyStatsDao
import com.app.java.trackingrunningapp.ui.data.DAOs.NotificationDao
import com.app.java.trackingrunningapp.ui.data.entities.PersonalGoal
import com.app.java.trackingrunningapp.ui.data.DAOs.PersonalGoalDao
import com.app.java.trackingrunningapp.ui.data.DAOs.RunSessionDao
import com.app.java.trackingrunningapp.ui.data.DAOs.TrainingPlanDao
import com.app.java.trackingrunningapp.ui.data.entities.User
import com.app.java.trackingrunningapp.ui.data.DAOs.UserDao
import com.app.java.trackingrunningapp.ui.data.entities.WeeklyStats
import com.app.java.trackingrunningapp.ui.data.DAOs.WeeklyStatsDao
import com.app.java.trackingrunningapp.ui.data.entities.YearlyStats
import com.app.java.trackingrunningapp.ui.data.DAOs.YearlyStatsDao
import com.app.java.trackingrunningapp.ui.data.entities.GPSPoint
import com.app.java.trackingrunningapp.ui.data.entities.GPSTrack
import com.app.java.trackingrunningapp.ui.data.entities.Notification
import com.app.java.trackingrunningapp.ui.data.entities.RunSession
import com.app.java.trackingrunningapp.ui.data.entities.TrainingPlan

@Database (
    entities =
        [
            User::class,
            PersonalGoal::class,
            WeeklyStats::class,
            MonthlyStats::class,
            YearlyStats::class,
            RunSession::class,
            TrainingPlan::class,
            GPSPoint::class,
            GPSTrack::class,
            Notification::class,
        ],
    version = 1,
    exportSchema = true
)
@TypeConverters(LocalTimeConverter::class)
abstract class RunningDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun personalGoalDao(): PersonalGoalDao
    abstract fun weeklyStatsDao(): WeeklyStatsDao
    abstract fun monthlyStatsDao(): MonthlyStatsDao
    abstract fun yearlyStatsDao(): YearlyStatsDao
    abstract fun runSessionDao(): RunSessionDao
    abstract fun trainingPlanDao(): TrainingPlanDao
    abstract fun GPSTrackDao(): GPSTrackDao
    abstract fun GPSPointDao(): GPSPointDao
    abstract fun notificationDao(): NotificationDao
}