package com.app.java.trackingrunningapp.data.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = GPSTrack::class,
            parentColumns = ["gpsTrackId"],
            childColumns = ["trackId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["trackId"])]
)
data class GPSPoint(
    @PrimaryKey(autoGenerate = true)
    val gpsPointId: Int = 0,
    val trackId: Int,
    var longitude: Double,
    var latitude: Double,
    val timeStamp: Long
)