package com.app.java.trackingrunningapp.ui.data.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Upsert
import com.app.java.trackingrunningapp.ui.data.entities.GPSPoint

@Dao
interface GPSPointDao {
    @Upsert
    suspend fun upsertGPSPoint(gpsPoint: GPSPoint)

    @Delete
    suspend fun deleteGPSPoint(gpsPoint: GPSPoint)
}