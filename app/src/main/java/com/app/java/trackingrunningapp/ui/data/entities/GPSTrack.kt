package com.app.java.trackingrunningapp.ui.data.entities

import android.media.metrics.LogSessionId
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

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
    var isPaused: Boolean = false,
)
