package com.app.java.trackingrunningapp.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = RunSession::class,
            parentColumns = ["sessionId"],
            childColumns = ["gpsSessionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["gpsSessionId"])]
)
data class GPSTrack(
    @PrimaryKey(autoGenerate = true)
    val gpsTrackId: Int,
    val gpsSessionId: Int,
    @ColumnInfo(defaultValue = "false") var isPaused: Boolean = false,
)
