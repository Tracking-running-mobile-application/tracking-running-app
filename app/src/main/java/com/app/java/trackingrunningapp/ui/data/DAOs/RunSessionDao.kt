package com.app.java.trackingrunningapp.ui.data.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Query
import androidx.room.Upsert
import com.app.java.trackingrunningapp.ui.data.entities.RunSession
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalTime
import com.app.java.trackingrunningapp.ui.data.entities.GPSPoint
import com.app.java.trackingrunningapp.ui.data.entities.GPSTrack

@Dao
interface RunSessionDao {
    @Delete
    suspend fun deleteRunSession(sessionId: Int)

    @Query("SELECT * FROM RunSession ORDER BY runDate DESC")
    suspend fun getAllRunSessions(): Flow<List<RunSession>>

    @Query("SELECT distance FROM RunSession WHERE sessionId = :sessionId")
    suspend fun getRunSessionDistanceById(sessionId: Int): Float

    @Query("SELECT * FROM RunSession WHERE isActive = TRUE LIMIT 1")
    suspend fun getCurrentRunSession(): RunSession?

    @Query("UPDATE RunSession SET runDate = :runDate WHERE sessionId = :sessionId ")
    suspend fun setRunDate(sessionId: Int, runDate: String)

    @Query(
        """
        UPDATE RunSession 
        SET 
            distance = :distance, 
            duration = :duration, 
            caloriesBurned = :caloriesBurned,
            pace = :pace
            
        WHERE sessionId = :sessionId
        """
    )
    suspend fun updateStatsSession(
        sessionId: Int,
        distance: Float,
        duration: Float,
        caloriesBurned: Float,
        pace: Float
    )

    @Query("UPDATE RunSession SET isActive = TRUE, endTime = :endTime WHERE sessionId = :sessionId")
    suspend fun finishRunSession(sessionId: Int, endTime: LocalTime)

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
    ): Flow<List<RunSession>>

    @Query("""
        SELECT * FROM RunSession AS rs    
        JOIN GPSTrack AS gt ON rs.sessionId = gt.sessionId
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


