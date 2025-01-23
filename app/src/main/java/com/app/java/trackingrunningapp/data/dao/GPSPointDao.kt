package com.app.java.trackingrunningapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.app.java.trackingrunningapp.data.model.entity.GPSPoint
import com.app.java.trackingrunningapp.data.model.dataclass.location.Location
import kotlinx.datetime.Instant

@Dao
interface GPSPointDao {
    @Insert
    suspend fun insertGPSPoint(gpsPoint: GPSPoint)

    //@Query("""
    //    SELECT longitude, latitude
    //    FROM GPSPoint
    //    WHERE trackId = :trackId
    //    ORDER BY timeStamp DESC
    //    LIMIT 2
    //""")
    //suspend fun getTwoLatestLocation(trackId: Int): List<Location>

    @Query("SELECT gpsPointId, longitude, latitude FROM GPSPoint WHERE trackId = :trackId AND gpsPointId > :gpsPointId")
    suspend fun getNewLocations(trackId: Int, gpsPointId: Int): List<Location>

    @Query("""
        SELECT gpsPointId, longitude, latitude
        FROM GPSPoint
        WHERE trackId = :trackId
        ORDER BY timeStamp DESC
    """)
    suspend fun getGPSPointByTrackId(trackId: Int): List<Location>
}