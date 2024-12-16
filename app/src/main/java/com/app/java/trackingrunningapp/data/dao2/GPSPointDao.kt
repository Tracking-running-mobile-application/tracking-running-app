package com.app.java.trackingrunningapp.data.dao2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.app.java.trackingrunningapp.data.model.entity.gps.GPSPoint
import com.app.java.trackingrunningapp.data.model.dataclass.location.Location

@Dao
interface GPSPointDao {
    @Insert
    suspend fun insertGPSPoint(gpsPoint: GPSPoint)

    @Query("""
        SELECT gps_point_longitude, gps_point_latitude
        FROM GPSPoint
        WHERE gps_point_track_id = :trackId
        ORDER BY gps_point_timestamp DESC
        LIMIT 2
    """)
    suspend fun getTwoLatestLocation(trackId: Int): List<Location>
}