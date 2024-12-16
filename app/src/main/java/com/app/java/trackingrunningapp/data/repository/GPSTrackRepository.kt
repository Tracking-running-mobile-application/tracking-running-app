package com.app.java.trackingrunningapp.data.repository

import com.app.java.trackingrunningapp.data.dao.GPSTrackDao
import com.app.java.trackingrunningapp.data.model.entity.gps.GPSTrack

class GPSTrackRepository(
    private val gpsTrackDao: GPSTrackDao
) {
    suspend fun setGPSTrackInactive(gpsTrackId: Int){
        gpsTrackDao.setGPSTrackInactive(gpsTrackId)
    }

    suspend fun setGPSTrackActive(gpsTrackId: Int){
        gpsTrackDao.setGPSTrackActive(gpsTrackId)
    }

    suspend fun getPauseOrContinueGPSTrack(gpsTrackId: Int){
        gpsTrackDao.getPauseOrContinueGPSTrack(gpsTrackId)
    }

    suspend fun deleteGPSTrack(gpsTrack: GPSTrack){
        gpsTrackDao.deleteGPSTrack(gpsTrack)
    }

    suspend fun createGPSTrack(gpsTrack: GPSTrack){
        gpsTrackDao.createGPSTrack(gpsTrack)
    }
}