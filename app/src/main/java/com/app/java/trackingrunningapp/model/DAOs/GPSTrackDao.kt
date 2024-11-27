package com.app.java.trackingrunningapp.model.DAOs

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.app.java.trackingrunningapp.model.entities.GPSTrack

@Dao
interface GPSTrackDao {
    @Query("UPDATE GPSTrack SET isPaused = TRUE WHERE gpsTrackId = :gpsTrackId")
    suspend fun pauseGPSTrack(gpsTrackId: Int)

    @Query("UPDATE GPSTrack SET isPaused = FALSE WHERE gpsTrackId = :gpsTrackId")
    suspend fun continueGPSTrack(gpsTrackId: Int)

    @Query("SELECT isPaused FROM GPSTrack WHERE gpsTrackId = :gpsTrackId")
    suspend fun pauseOrContinueGPSTrack(gpsTrackId: Int): Boolean

    @Query("DELETE FROM GPSTrack WHERE gpsTrackId = :gpsTrackId")
    suspend fun deleteGPSTrack(gpsTrackId: Int)

    @Query("UPDATE GPSTrack SET gpsSessionId = :gpsSessionId WHERE gpsTrackId = :gpsTrackId")
    suspend fun insertGPSTrack(gpsTrackId: Int, gpsSessionId: Int)

    @Query("SELECT gpsTrackId FROM GPSTrack WHERE gpsSessionId = :sessionId")
    suspend fun getGPSTrackIdBySessionId(sessionId: Int): Int?
}