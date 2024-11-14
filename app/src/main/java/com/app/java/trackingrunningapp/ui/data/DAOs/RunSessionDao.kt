package com.app.java.trackingrunningapp.ui.data.DAOs

import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.app.java.trackingrunningapp.ui.data.entities.RunSession
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalTime

interface RunSessionDao {
    @Upsert
    suspend fun upsertRunSession(runSession: RunSession)

    @Delete
    suspend fun deleteRunSession(runSession: RunSession)

    @Query("SELECT * FROM RunSession ORDER BY runDate DESC")
    suspend fun getAllRunSessions(): Flow<List<RunSession>>

    @Query("SELECT distance FROM RunSession WHERE sessionId = :sessionId")
    suspend fun getRunSessionById(sessionId: Int): Float

    @Query("SELECT sessionId FROM RunSession WHERE isActive = True LIMIT 1")
    suspend fun getCurrentRunSession(isActive: Boolean): RunSession?

    @Query("UPDATE RunSession SET distance = :distance, duration = :duration, caloriesBurned = :caloriesBurned")
    suspend fun updateStatsSession(distance: Float, duration: Float, caloriesBurned: Float)

    @Query("UPDATE RunSession SET isActive = False, endTime = :endTime WHERE sessionId = :sessionId")
    suspend fun finishRunSession(sessionId: Int, endTime: LocalTime)
}