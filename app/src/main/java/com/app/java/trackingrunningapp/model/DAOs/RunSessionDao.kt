package com.app.java.trackingrunningapp.model.DAOs

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Query
import androidx.room.Upsert
import com.app.java.trackingrunningapp.model.entities.GPSPoint
import com.app.java.trackingrunningapp.model.entities.GPSTrack
import com.app.java.trackingrunningapp.model.entities.RunSession

@Dao
interface RunSessionDao {
    @Query("DELETE FROM runsession WHERE sessionId = :sessionId")
    suspend fun deleteRunSession(sessionId: Int)

    @Query(
        """
        SELECT * 
        FROM RunSession 
        ORDER BY runDate DESC
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

    @Query("SELECT * FROM RunSession WHERE isActive = TRUE LIMIT 1")
    suspend fun getCurrentRunSession(): RunSession?

    @Query("SELECT * FROM RUNSESSION WHERE sessionId = :sessionId")
    suspend fun getRunSessionById(sessionId: Int): RunSession?

    @Query("UPDATE RunSession SET caloriesBurned = :caloriesBurned WHERE sessionId =:sessionId")
    suspend fun updateCaloriesBurnedSession(sessionId: Int, caloriesBurned: Double)

    @Query("UPDATE RunSession SET pace = :pace WHERE sessionId =:sessionId")
    suspend fun updatePaceSession(sessionId: Int, pace: Double)

    @Query("UPDATE RunSession SET duration = :duration WHERE sessionId =:sessionId")
    suspend fun updateDurationSession(sessionId: Int, duration: Long)

    @Query("UPDATE RunSession SET distance = :distance WHERE sessionId =:sessionId")
    suspend fun updateDistanceSession(sessionId: Int, distance: Double)

    @Query("UPDATE RunSession SET isActive = TRUE, runDate = :runDate WHERE sessionId = :sessionId")
    suspend fun startRunSession(sessionId: Int, runDate: String)

    @Query("UPDATE RunSession SET isActive = FALSE WHERE sessionId = :sessionId")
    suspend fun finishRunSession(sessionId: Int)

    @Query("UPDATE RunSession SET isFavorite = TRUE, dateAddInFavorite = :dateAddInFavorite WHERE sessionId = :sessionId")
    suspend fun addFavoriteRunSession(sessionId: Int, dateAddInFavorite: String)

    @Query("UPDATE RunSession SET isFavorite = FALSE, dateAddInFavorite = null WHERE sessionId = :sessionId")
    suspend fun removeFavoriteRunSession(sessionId: Int)

    @Query(
        """
        SELECT *
        FROM RunSession
        WHERE runDate BETWEEN :startDate AND :endDate
        """
    )
    suspend fun filterRunningSessionByDay(
        startDate: String,
        endDate: String
    ): List<RunSession>

    @Upsert
    suspend fun createMockDataForRunSession(runSession: RunSession)

    @Query("""
        SELECT * FROM RunSession AS rs    
        JOIN GPSTrack AS gt ON rs.sessionId = gt.gpsSessionId
        JOIN GPSPoint AS gp ON gt.gpsTrackId = gp.gpsPointId
        WHERE rs.sessionId = :sessionId
    """)
    suspend fun getCompleteSessionData(sessionId: Int): List<CompleteSessionData>

    data class CompleteSessionData (
        @Embedded val runSession: RunSession,
        @Embedded val gpsTrack: GPSTrack,
        @Embedded val gpsPoint: GPSPoint
    )
}


