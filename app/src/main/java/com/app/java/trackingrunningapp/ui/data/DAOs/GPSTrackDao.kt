package com.app.java.trackingrunningapp.ui.data.DAOs

import androidx.room.Dao
import androidx.room.Query

@Dao
interface GPSTrackDao {
    @Query("UPDATE GPSTrack SET isPaused = TRUE WHERE gpsTrackId = :gpsTrackId")
    suspend fun pauseGPSTrack(gpsTrackId: Int)

    @Query("UPDATE GPSTrack SET isPaused = FALSE WHERE gpsTrackId = :gpsTrackId")
    suspend fun continueGPSTrack(gpsTrackId: Int)

    @Query("SELECT isPaused FROM GPSTrack WHERE gpsTrackId = :gpsTrackId")
    suspend fun pauseOrContinueGPSTrack(gpsTrackId: Int): Boolean
}