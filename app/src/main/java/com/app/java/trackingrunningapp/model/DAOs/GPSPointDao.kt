package com.app.java.trackingrunningapp.model.DAOs

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.app.java.trackingrunningapp.model.entities.GPSPoint

@Dao
interface GPSPointDao {
    @Upsert
    suspend fun upsertGPSPoint(gpsPoint: GPSPoint)

    @Query("DELETE FROM gpspoint WHERE gpsPointId = :gpsPointId")
    suspend fun deleteGPSPoint(gpsPointId: Int)
}