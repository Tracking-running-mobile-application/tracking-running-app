package com.app.java.trackingrunningapp.model.DAOs

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.app.java.trackingrunningapp.model.entities.GPSPoint
import com.app.java.trackingrunningapp.model.models.Location

@Dao
interface GPSPointDao {
    @Query("DELETE FROM GPSPoint WHERE gpsPointId = :gpsPointId")
    suspend fun deleteGPSPoint(gpsPointId: Int)

    @Insert
    suspend fun insertGPSPoint(gpsPoint: GPSPoint)

    @Query("""
        SELECT longitude, latitude
        FROM GPSPoint
        WHERE trackId = :trackId
        ORDER BY timeStamp DESC
        LIMIT 2
    """)
    suspend fun getTwoLatestLocation(trackId: Int): List<Location>
}