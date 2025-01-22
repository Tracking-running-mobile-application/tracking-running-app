package com.app.java.trackingrunningapp.data.dao

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Query
import com.app.java.trackingrunningapp.data.model.entity.GPSPoint
import com.app.java.trackingrunningapp.data.model.entity.GPSTrack
import com.app.java.trackingrunningapp.data.model.entity.RunSession
import com.app.java.trackingrunningapp.data.model.dataclass.location.StatsSession

@Dao
interface RunSessionDao {
    @Query("DELETE FROM runsession WHERE sessionId = :sessionId")
    suspend fun deleteRunSession(sessionId: Int)

    @Query(
        """
        SELECT * 
        FROM RunSession 
        ORDER BY runDate DESC, sessionId DESC
        LIMIT :limit OFFSET :offset
        """)
    suspend fun getAllRunSessions(limit: Int, offset: Int): List<RunSession>

    @Query(
        """
        SELECT * 
        FROM RunSession 
        WHERE isFavorite = 1
        AND dateAddInFavorite IS NOT NULL
        ORDER BY dateAddInFavorite DESC
        """
    )
    suspend fun getAllFavoriteRunSessions(): List<RunSession>

    @Query("SELECT * FROM RunSession WHERE isActive = TRUE ORDER BY sessionId DESC LIMIT 1")
    suspend fun getCurrentRunSession(): RunSession?

    @Query("SELECT * FROM RUNSESSION WHERE sessionId = :sessionId")
    suspend fun getRunSessionById(sessionId: Int): RunSession?

    @Query("UPDATE RunSession SET distance = :distance, duration = :duration, caloriesBurned = :caloriesBurned, pace = :pace WHERE sessionId = :sessionId")
    suspend fun updateStatsSession(sessionId: Int, distance: Double, duration: Long, caloriesBurned: Double, pace: Double)

    @Insert
    suspend fun initiateRunSession(runSession: RunSession)

    @Query("UPDATE RunSession SET isActive = FALSE WHERE sessionId = :sessionId")
    suspend fun setRunSessionInactive(sessionId: Int)

    @Query("UPDATE RunSession SET isFavorite = TRUE, dateAddInFavorite = :dateAddInFavorite WHERE sessionId = :sessionId")
    suspend fun addFavoriteRunSession(sessionId: Int, dateAddInFavorite: String)

    @Query("UPDATE RunSession SET isFavorite = FALSE, dateAddInFavorite = null WHERE sessionId = :sessionId")
    suspend fun removeFavoriteRunSession(sessionId: Int)

    @Query(
        """
        SELECT *
        FROM RunSession
        WHERE runDate BETWEEN :startDate AND :endDate ORDER BY sessionId DESC
        """
    )
    suspend fun filterRunningSessionByDay(
        startDate: String,
        endDate: String
    ): List<RunSession>

    @Query("""
        SELECT duration, distance, pace, caloriesBurned
        FROM runsession
        WHERE sessionId = :sessionId
    """)
    suspend fun fetchStatsSession(sessionId: Int): StatsSession
}


