package com.app.java.trackingrunningapp.data.dao2

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update

@Dao
interface GPSTrackDao {
    @Query("UPDATE GPSTrack SET gps_track_is_paused = TRUE WHERE gps_track_id = :gpsTrackId")
    suspend fun setGPSTrackInactive(gpsTrackId: Int)

    @Query("UPDATE GPSTrack SET gps_track_is_paused = FALSE WHERE gps_track_id = :gpsTrackId")
    suspend fun setGPSTrackActive(gpsTrackId: Int)

    @Query("SELECT gps_track_is_paused FROM GPSTrack WHERE gps_track_id = :gpsTrackId")
    suspend fun getPauseOrContinueGPSTrack(gpsTrackId: Int): Boolean

    @Delete
    suspend fun deleteGPSTrack(gpsTrackId: Int):Long

    @Update
    suspend fun createGPSTrack(gpsTrackId: Int):Long

}