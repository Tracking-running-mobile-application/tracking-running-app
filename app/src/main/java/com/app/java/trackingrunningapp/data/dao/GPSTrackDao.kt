package com.app.java.trackingrunningapp.data.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface GPSTrackDao {
    @Query("UPDATE GPSTrack SET isPaused = TRUE WHERE gpsTrackId = :gpsTrackId")
    suspend fun setGPSTrackInactive(gpsTrackId: Int)

    @Query("UPDATE GPSTrack SET isPaused = FALSE WHERE gpsTrackId = :gpsTrackId")
    suspend fun setGPSTrackActive(gpsTrackId: Int)

    @Query("SELECT isPaused FROM GPSTrack WHERE gpsTrackId = :gpsTrackId")
    suspend fun pauseOrContinueGPSTrack(gpsTrackId: Int): Boolean

    @Query("DELETE FROM GPSTrack WHERE gpsTrackId = :gpsTrackId")
    suspend fun deleteGPSTrack(gpsTrackId: Int)

    @Query("UPDATE GPSTrack SET gpsSessionId = :gpsSessionId WHERE gpsTrackId = :gpsTrackId")
    suspend fun createGPSTrack(gpsTrackId: Int, gpsSessionId: Int)

    @Query("SELECT gpsTrackId FROM GPSTrack WHERE gpsSessionId = :sessionId")
    suspend fun getGPSTrackIdBySessionId(sessionId: Int): Int?
}