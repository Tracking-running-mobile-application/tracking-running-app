package com.app.java.trackingrunningapp.ui.data.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.app.java.trackingrunningapp.ui.data.entities.GPSTrack

@Dao
interface GPSTrackDao {
    @Query("UPDATE GPSTrack SET isPaused = TRUE WHERE gpsTrackId = :gpsTrackId")
    suspend fun pauseGPSTrack(gpsTrackId: Int)

    @Query("UPDATE GPSTrack SET isPaused = FALSE WHERE gpsTrackId = :gpsTrackId")
    suspend fun continueGPSTrack(gpsTrackId: Int)

    @Query("SELECT isPaused FROM GPSTrack WHERE gpsTrackId = :gpsTrackId")
    suspend fun pauseOrContinueGPSTrack(gpsTrackId: Int): Boolean

    @Delete
    suspend fun deleteGPSTrack(gpsTrackId: Int)

    @Upsert
    suspend fun upsertGPSTrack(gpsTrack: GPSTrack)
}