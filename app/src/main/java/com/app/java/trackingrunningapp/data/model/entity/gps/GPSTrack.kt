package com.app.java.trackingrunningapp.data.model.entity.gps

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.app.java.trackingrunningapp.data.model.entity.goal.RunSession

@Entity("GPSTrack")
data class GPSTrack(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("gps_track_id") val gpsTrackId: Int, // PK
    @ColumnInfo("gps_track_session_id") val gpsSessionId: Int, // FK RunSession
    @ColumnInfo("gps_track_is_paused") var isPaused: Boolean = false,
)
