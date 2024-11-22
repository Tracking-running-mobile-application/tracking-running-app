package com.app.java.trackingrunningapp.ui.data.entities

import android.media.metrics.LogSessionId
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = RunSession::class,
            parentColumns = ["sessionId"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GPSTrack(
    @PrimaryKey(autoGenerate = true)
    val gpsTrackId: Int,
    val sessionId: Int,
    var isPaused: Boolean = false,
)
