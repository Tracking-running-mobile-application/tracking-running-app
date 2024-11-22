package com.app.java.trackingrunningapp.modelbase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.java.trackingrunningapp.model.DAOs.GPSPointDao
import com.app.java.trackingrunningapp.model.DAOs.GPSTrackDao
import com.app.java.trackingrunningapp.model.converters.LocalTimeConverter
import com.app.java.trackingrunningapp.model.entities.MonthlyStats
import com.app.java.trackingrunningapp.model.DAOs.MonthlyStatsDao
import com.app.java.trackingrunningapp.model.DAOs.NotificationDao
import com.app.java.trackingrunningapp.model.entities.PersonalGoal
import com.app.java.trackingrunningapp.model.DAOs.PersonalGoalDao
import com.app.java.trackingrunningapp.model.DAOs.RunSessionDao
import com.app.java.trackingrunningapp.model.DAOs.TrainingPlanDao
import com.app.java.trackingrunningapp.model.entities.User
import com.app.java.trackingrunningapp.model.DAOs.UserDao
import com.app.java.trackingrunningapp.model.entities.WeeklyStats
import com.app.java.trackingrunningapp.model.DAOs.WeeklyStatsDao
import com.app.java.trackingrunningapp.model.entities.YearlyStats
import com.app.java.trackingrunningapp.model.DAOs.YearlyStatsDao
import com.app.java.trackingrunningapp.model.entities.GPSPoint
import com.app.java.trackingrunningapp.model.entities.GPSTrack
import com.app.java.trackingrunningapp.model.entities.Notification
import com.app.java.trackingrunningapp.model.entities.RunSession
import com.app.java.trackingrunningapp.model.entities.TrainingPlan

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