package com.app.java.trackingrunningapp.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.java.trackingrunningapp.data.dao.GPSPointDao
import com.app.java.trackingrunningapp.data.dao.GPSTrackDao
import com.app.java.trackingrunningapp.data.dao.MonthlyStatsDao
import com.app.java.trackingrunningapp.data.model.entity.MonthlyStats
import com.app.java.trackingrunningapp.data.dao.NotificationDao
import com.app.java.trackingrunningapp.data.model.entity.PersonalGoal
import com.app.java.trackingrunningapp.data.dao.PersonalGoalDao
import com.app.java.trackingrunningapp.data.dao.RunSessionDao
import com.app.java.trackingrunningapp.data.dao.TrainingPlanDao
import com.app.java.trackingrunningapp.data.dao.UserDao
import com.app.java.trackingrunningapp.data.dao.WeeklyStatsDao
import com.app.java.trackingrunningapp.data.dao.YearlyStatsDao
import com.app.java.trackingrunningapp.utils.LocalTimeConverter
import com.app.java.trackingrunningapp.data.model.entity.GPSPoint
import com.app.java.trackingrunningapp.data.model.entity.User
import com.app.java.trackingrunningapp.data.model.entity.WeeklyStats
import com.app.java.trackingrunningapp.data.model.entity.YearlyStats
import com.app.java.trackingrunningapp.data.model.entity.GPSTrack
import com.app.java.trackingrunningapp.data.model.entity.Notification
import com.app.java.trackingrunningapp.data.model.entity.RunSession
import com.app.java.trackingrunningapp.data.model.entity.TrainingPlan

@Database(
    entities = [
        User::class,
        PersonalGoal::class,
        WeeklyStats::class,
        MonthlyStats::class,
        YearlyStats::class,
        RunSession::class,
        TrainingPlan::class,
        GPSPoint::class,
        GPSTrack::class,
        Notification::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(LocalTimeConverter::class)
abstract class RunningDatabase : RoomDatabase() {
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

    companion object {
        @Volatile
        private var INSTANCE: RunningDatabase? = null

        fun getInstance(context: Context): RunningDatabase =
            INSTANCE ?: synchronized(this) {
                Log.d("Database", "Data being initialised")
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    RunningDatabase::class.java,
                    "running_database"
                )
                    .createFromAsset("database/initial_data.db")
                    .build().also { INSTANCE = it }
            }
    }
}
