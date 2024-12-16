package com.app.java.trackingrunningapp.data.dao2

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Query
import com.app.java.trackingrunningapp.data.model.entity.gps.GPSPoint
import com.app.java.trackingrunningapp.data.model.entity.gps.GPSTrack
import com.app.java.trackingrunningapp.data.model.entity.goal.RunSession
import com.app.java.trackingrunningapp.data.model.dataclass.location.StatsSession

@Dao
interface RunSessionDao {
    @Delete
    suspend fun deleteRunSession(sessionId: Int):Long

    @Query(""" SELECT * FROM RunSession ORDER BY run_date DESC LIMIT :limit OFFSET :offset """)
    suspend fun getAllRunSessions(limit: Int, offset: Int): List<RunSession>

    @Query(
        """
        SELECT * 
        FROM RunSession 
        WHERE is_favorite = 1
        AND date_add_in_favorite IS NOT NULL
        ORDER BY date_add_in_favorite DESC
        """
    )
    suspend fun getAllFavoriteRunSessions(): List<RunSession>

    @Query("SELECT * FROM RunSession WHERE is_active = TRUE LIMIT 1")
    suspend fun getCurrentRunSession(): RunSession?

    @Query("SELECT * FROM RunSession WHERE session_id = :sessionId")
    suspend fun getRunSessionById(sessionId: Int): RunSession?

    /***
     * TODO: Change this to update stats!
     * ***/
    @Query("UPDATE RunSession SET calories_burned = :caloriesBurned WHERE session_id =:sessionId")
    suspend fun updateCaloriesBurnedSession(sessionId: Int, caloriesBurned: Double)

    @Query("UPDATE RunSession SET pace = :pace WHERE session_id =:sessionId")
    suspend fun updatePaceSession(sessionId: Int, pace: Double)

    @Query("UPDATE RunSession SET duration = :duration WHERE session_id =:sessionId")
    suspend fun updateDurationSession(sessionId: Int, duration: Long)

    @Query("UPDATE RunSession SET distance = :distance WHERE session_id =:sessionId")
    suspend fun updateDistanceSession(sessionId: Int, distance: Double)

    @Query("UPDATE RunSession SET distance = :distance, duration = :duration, calories_burned = :caloriesBurned, pace = :pace WHERE session_id = :sessionId")
    suspend fun updateStatsSession(sessionId: Int, distance: Double, duration: Long, caloriesBurned: Double, pace: Double)

    @Query("UPDATE RunSession SET is_active = TRUE, run_date = :runDate WHERE session_id = :sessionId")
    suspend fun initiateRunSession(sessionId: Int, runDate: String)

    @Query("UPDATE RunSession SET is_active = FALSE WHERE session_id = :sessionId")
    suspend fun setRunSessionInactive(sessionId: Int)

    @Query("UPDATE RunSession SET is_favorite = TRUE, date_add_in_favorite = :dateAddInFavorite WHERE session_id = :sessionId")
    suspend fun addFavoriteRunSession(sessionId: Int, dateAddInFavorite: String)

    @Query("UPDATE RunSession SET is_favorite = FALSE, date_add_in_favorite = null WHERE session_id = :sessionId")
    suspend fun removeFavoriteRunSession(sessionId: Int)

    @Query(
        """
        SELECT *
        FROM RunSession
        WHERE run_date BETWEEN :startDate AND :endDate
        """
    )
    suspend fun filterRunningSessionByDay(
        startDate: String,
        endDate: String
    ): List<RunSession>

    @Query("""
        SELECT duration, distance, pace, calories_burned
        FROM runsession
        WHERE session_id = :sessionId
    """)
    suspend fun fetchStatsSession(sessionId: Int): StatsSession

    @Query("""
        SELECT * FROM RunSession AS rs    
        JOIN GPSTrack AS gt ON rs.session_id = gt.gps_track_session_id
        JOIN GPSPoint AS gp ON gt.gps_track_id = gp.gps_point_track_id
        WHERE rs.session_id = :sessionId
    """)
    suspend fun getCompleteSessionData(sessionId: Int): List<CompleteSessionData>

    data class CompleteSessionData (
        @Embedded val runSession: RunSession,
        @Embedded val gpsTrack: GPSTrack,
        @Embedded val gpsPoint: GPSPoint
    )
}


