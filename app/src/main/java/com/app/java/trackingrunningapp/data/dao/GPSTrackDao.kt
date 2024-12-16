package com.app.java.trackingrunningapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import com.app.java.trackingrunningapp.data.model.entity.gps.GPSTrack

@Dao
interface GPSTrackDao {
    @Query("UPDATE GPSTrack SET gps_track_is_paused = TRUE WHERE gps_track_id = :gpsTrackId")
    suspend fun setGPSTrackInactive(gpsTrackId: Int)

    @Query("UPDATE GPSTrack SET gps_track_is_paused = FALSE WHERE gps_track_id = :gpsTrackId")
    suspend fun setGPSTrackActive(gpsTrackId: Int)

    @Query("SELECT gps_track_is_paused FROM GPSTrack WHERE gps_track_id = :gpsTrackId")
    suspend fun getPauseOrContinueGPSTrack(gpsTrackId: Int): Boolean

    @Delete
    suspend fun deleteGPSTrack(gpsTrack: GPSTrack)

    @Update
    suspend fun createGPSTrack(gpsTrack: GPSTrack)

}