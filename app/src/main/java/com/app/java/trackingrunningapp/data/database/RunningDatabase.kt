package com.app.java.trackingrunningapp.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.java.trackingrunningapp.data.dao.GPSPointDao
import com.app.java.trackingrunningapp.data.dao.GPSTrackDao
import com.app.java.trackingrunningapp.data.model.entity.stat.MonthlyStats
import com.app.java.trackingrunningapp.data.dao.NotificationDao
import com.app.java.trackingrunningapp.data.model.entity.goal.PersonalGoal
import com.app.java.trackingrunningapp.data.dao.PersonalGoalDao
import com.app.java.trackingrunningapp.data.dao.RunSessionDao
import com.app.java.trackingrunningapp.data.dao.StatsDao
import com.app.java.trackingrunningapp.data.dao.TrainingPlanDao
import com.app.java.trackingrunningapp.data.dao.UserDao
import com.app.java.trackingrunningapp.utils.LocalTimeConverter
import com.app.java.trackingrunningapp.data.model.entity.gps.GPSPoint
import com.app.java.trackingrunningapp.data.model.entity.user.User
import com.app.java.trackingrunningapp.data.model.entity.stat.WeeklyStats
import com.app.java.trackingrunningapp.data.model.entity.stat.YearlyStats
import com.app.java.trackingrunningapp.data.model.entity.gps.GPSTrack
import com.app.java.trackingrunningapp.data.model.entity.notification.Notification
import com.app.java.trackingrunningapp.data.model.entity.goal.RunSession
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
    abstract fun statsDao():StatsDao
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
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
    }
}
