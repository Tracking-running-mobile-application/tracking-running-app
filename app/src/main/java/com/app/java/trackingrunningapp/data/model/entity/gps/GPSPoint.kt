package com.app.java.trackingrunningapp.data.model.entity.gps

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity("GPSPoint")
data class GPSPoint(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("gps_point_id") val gpsPointId: Int, // PK
    @ColumnInfo("gps_point_track_id") val trackId: Int, // FK GPSTrack
    @ColumnInfo("gps_point_longitude") var longitude: Double,
    @ColumnInfo("gps_point_latitude") var latitude: Double,
    @ColumnInfo("gps_point_timestamp") val timeStamp: Long
)