package com.app.java.trackingrunningapp.modelbase

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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

    companion object {
        @Volatile
        private var INSTANCE: RunningDatabase? = null

        fun getInstance(context: Context): RunningDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it}
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                RunningDatabase::class.java, "running_database")
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        val job = CoroutineScope(Dispatchers.IO).launch {
                            try {
                            db.execSQL(
                            """
                            INSERT INTO TrainingPlan 
                            (planSessionId, title, description, estimatedTime, targetDistance, targetDuration, 
                            targetCaloriesBurned, goalProgress, exerciseType, difficulty, lastRecommendedDate, isFinished) 
                            VALUES 
                            (1, 'Running for Beginners', 'This plan helps new runners build a solid foundation of endurance and stamina while gradually increasing their distances.', 30.0, 5.0, NULL, NULL, 0.0, 'Running', 'Beginner', NULL, 0),
                            (2, 'Trail Hiking Basics', 'Perfect for those starting their hiking journey, focusing on short distances and light trails to improve overall fitness.', 60.0, 2.0, NULL, NULL, 0.0, 'Hiking', 'Beginner', NULL, 0),
                            (3, 'Mindful Movement', 'Combine light running with mindfulness exercises to improve focus and reduce stress while staying active.', 20.0, NULL, 20.0, NULL, 0.0, 'Mindful Running', 'Beginner', NULL, 0),
                            (4, 'Strength Training for Trails', 'This plan introduces basic strength exercises to help trail runners navigate uneven terrains safely.', 45.0, NULL, NULL, 200.0, 0.0, 'Trail Running', 'Beginner', NULL, 0),
                            (5, 'Trail Exploration Start', 'Begin exploring trails with manageable distances to build your endurance and adapt to new surfaces.', 70.0, 10.0, NULL, NULL, 0.0, 'Trail Running', 'Beginner', NULL, 0),
                            (6, 'Easy Running Plan', 'Develop a consistent running habit by aligning breath and stride for better endurance and mental clarity.', 30.0, NULL, 30.0, NULL, 0.0, 'Running', 'Beginner', NULL, 0),
                            (7, 'Hiking Preparation', 'Build the necessary stamina and strength for day-long hiking adventures with a focus on light trails.', 90.0, 3.0, NULL, NULL, 0.0, 'Hiking', 'Beginner', NULL, 0),
                            (8, 'Introduction to Running', 'A beginner-friendly approach to running, focusing on consistency and gradual progression to cover moderate distances.', 35.0, 5.0, NULL, NULL, 0.0, 'Running', 'Beginner', NULL, 0),
                            (9, 'Trail Basics', 'Learn the essentials of trail running, including handling varied terrains and maintaining proper balance.', 50.0, 6.0, NULL, NULL, 0.0, 'Trail Running', 'Beginner', NULL, 0),
                            (10, 'Mindful Jogging', 'Focus on syncing your breathing with movement to enhance your running efficiency and mental focus.', 15.0, NULL, 15.0, NULL, 0.0, 'Mindful Running', 'Beginner', NULL, 0),
                            (11, 'Distance Builder', 'Enhance your endurance through structured runs aimed at gradually increasing the distance you can cover.', 60.0, 10.0, NULL, NULL, 0.0, 'Running', 'Intermediate', NULL, 0),
                            (12, 'Trail Endurance Plan', 'Prepare for trails with significant elevation changes through targeted exercises to build stamina and strength.', 75.0, NULL, NULL, 400.0, 0.0, 'Trail Running', 'Intermediate', NULL, 0),
                            (13, 'Stamina for Long Runs', 'Focus on long, steady runs to improve endurance and prepare for covering greater distances.', 90.0, 15.0, NULL, NULL, 0.0, 'Running', 'Intermediate', NULL, 0),
                            (14, 'Trail Fitness Plan', 'Challenge yourself with steady trail runs to improve your stamina and overall trail performance.', 80.0, 12.0, NULL, NULL, 0.0, 'Trail Running', 'Intermediate', NULL, 0),
                            (15, 'Long Distance Focus', 'Work on pacing and energy management for longer durations, perfect for runners aiming to push their limits.', 45.0, NULL, 45.0, NULL, 0.0, 'Running', 'Intermediate', NULL, 0),
                            (16, 'Mindful Endurance', 'Practice mindful techniques while running to help you stay focused and reduce fatigue during longer runs.', 30.0, NULL, 30.0, NULL, 0.0, 'Mindful Running', 'Intermediate', NULL, 0),
                            (17, 'Advanced Trail Exploration', 'Master trail techniques like handling steep inclines and sharp descents to improve trail running performance.', 100.0, 10.0, NULL, NULL, 0.0, 'Trail Running', 'Intermediate', NULL, 0),
                            (18, 'Trail Climbing Strength', 'Boost your strength with targeted hill climbing exercises designed for tackling trails with varying elevations.', 90.0, NULL, NULL, 450.0, 0.0, 'Trail Running', 'Intermediate', NULL, 0),
                            (19, 'Focused Running', 'Combine mindful exercises with your runs to build focus and reduce fatigue, enhancing your overall running experience.', 40.0, NULL, 40.0, NULL, 0.0, 'Mindful Running', 'Intermediate', NULL, 0),
                            (20, 'Trail Endurance Challenge', 'Push your trail fitness with runs that test your endurance and help you adapt to challenging surfaces.', 85.0, 12.0, NULL, NULL, 0.0, 'Trail Running', 'Intermediate', NULL, 0),
                            (21, 'Advanced Distance Training', 'Prepare for advanced distances with structured long runs, speed intervals, and expert pacing strategies.', 120.0, 21.1, NULL, NULL, 0.0, 'Running', 'Advanced', NULL, 0),
                            (22, 'Marathon Preparation', 'Develop the endurance needed for marathons with a plan focused on gradual distance increases and recovery.', 240.0, 42.2, NULL, NULL, 0.0, 'Running', 'Advanced', NULL, 0),
                            (23, 'Trail Mastery', 'Learn advanced trail techniques to handle technical trails with steep descents, sharp turns, and rough surfaces.', 150.0, 20.0, NULL, NULL, 0.0, 'Trail Running', 'Advanced', NULL, 0),
                            (24, 'Endurance Running', 'Enhance your stamina for ultra-distance events with a mix of long runs and focused interval training.', 180.0, 50.0, NULL, NULL, 0.0, 'Running', 'Advanced', NULL, 0),
                            (25, 'Mindful Ultra Training', 'Strengthen your mental focus and reduce fatigue during ultra-distance runs by practicing advanced mindfulness techniques.', 90.0, NULL, 90.0, NULL, 0.0, 'Mindful Running', 'Advanced', NULL, 0),
                            (26, 'Trail Marathon Preparation', 'Train for long trail marathons with a focus on endurance, pacing, and handling technical terrains.', 200.0, 42.2, NULL, NULL, 0.0, 'Trail Running', 'Advanced', NULL, 0),
                            (27, 'Elite Hiking Endurance', 'Prepare for multi-day hiking adventures with targeted exercises to build strength and long-lasting endurance.', 240.0, 20.0, NULL, NULL, 0.0, 'Hiking', 'Advanced', NULL, 0),
                            (28, 'Marathon Excellence', 'Focus on advanced strategies and training to achieve peak performance in competitive marathon events.', 240.0, 42.2, NULL, NULL, 0.0, 'Running', 'Advanced', NULL, 0),
                            (29, 'Trail Climbing Mastery', 'Master steep climbs and descents with advanced training techniques to boost power and adaptability.', 180.0, 30.0, NULL, NULL, 0.0, 'Trail Running', 'Advanced', NULL, 0),
                            (30, 'Ultra Running Focus', 'Develop both the physical and mental endurance needed for ultra-distance running events with this advanced training.', 300.0, NULL, 120.0, NULL, 0.0, 'Mindful Running', 'Advanced', NULL, 0);
                            """
                            )

                            db.execSQL(
                                """
                            INSERT INTO RunSession 
                            (runDate, distance, duration, pace, caloriesBurned, isActive, dateAddInFavorite, isFavorite) 
                            VALUES 
                            ('2024-11-23', 5.5, 1800, 5.8, 350, 0, NULL, 0),
                            ('2024-11-24', 6.0, 2000, 5.7, 400, 0, NULL, 0),
                            ('2024-11-25', 7.0, 2300, 5.5, 450, 0, NULL, 0),
                            ('2024-11-26', 4.5, 1600, 6.2, 300, 0, NULL, 0),
                            ('2024-11-27', 6.2, 2100, 5.6, 410, 0, NULL, 0),
                            ('2024-11-28', 5.8, 1900, 5.9, 370, 0, NULL, 1),
                            ('2024-11-29', 7.5, 2500, 5.4, 500, 0, NULL, 0),
                            ('2024-11-30', 5.0, 1800, 6.0, 350, 0, NULL, 0),
                            ('2024-12-01', 6.8, 2200, 5.5, 420, 0, NULL, 0),
                            ('2024-12-02', 7.0, 2400, 5.4, 480, 0, NULL, 0),
                            ('2024-12-03', 5.6, 1900, 5.8, 360, 0, NULL, 0),
                            ('2024-12-04', 6.3, 2100, 5.6, 410, 0, NULL, 0),
                            ('2024-12-05', 4.8, 1700, 6.2, 310, 0, NULL, 1),
                            ('2024-12-06', 5.2, 1800, 6.1, 320, 0, NULL, 0),
                            ('2024-12-07', 6.7, 2300, 5.5, 430, 0, NULL, 0),
                            ('2024-12-08', 7.2, 2500, 5.4, 490, 0, NULL, 0),
                            ('2024-12-09', 5.9, 2000, 5.8, 380, 0, NULL, 0),
                            ('2024-12-10', 6.1, 2100, 5.7, 400, 0, NULL, 0),
                            ('2024-12-11', 4.9, 1700, 6.3, 320, 0, NULL, 0),
                            ('2024-12-12', 5.4, 1900, 6.0, 340, 0, NULL, 0),
                            ('2024-12-13', 6.5, 2200, 5.6, 420, 0, NULL, 0),
                            ('2024-12-14', 5.7, 2000, 5.9, 370, 0, NULL, 0),
                            ('2024-12-14', 7.3, 2500, 5.4, 500, 0, NULL, 0),
                            ('2024-12-16', 6.0, 2100, 5.7, 400, 0, NULL, 1),
                            ('2024-12-17', 5.3, 1900, 6.0, 350, 0, NULL, 0),
                            ('2024-12-18', 7.0, 2400, 5.5, 470, 0, NULL, 0),
                            ('2024-12-19', 5.8, 2000, 5.8, 380, 0, NULL, 0),
                            ('2024-12-21', 6.4, 2200, 5.6, 420, 0, NULL, 0),
                            ('2024-12-21', 4.7, 1600, 6.3, 300, 0, NULL, 0),
                            ('2024-12-21', 2.7, 1100, 3.3, 400, 0, NULL, 0),
                            ('2024-12-22', 5.1, 1800, 6.1, 320, 0, NULL, 1); 
                            """
                            )
                        } catch(e: Exception) {
                            Log.e("Error", "Insert Failed!", e)
                        }
                    }
                        runBlocking { job.join() }
                }
            }
        ).build()
    }
}
