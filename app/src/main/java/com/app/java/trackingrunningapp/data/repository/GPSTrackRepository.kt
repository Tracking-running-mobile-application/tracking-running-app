package com.app.java.trackingrunningapp.data.repository

import com.app.java.trackingrunningapp.data.dao2.GPSTrackDao

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

    suspend fun deleteGPSTrack(gpsTrackId: Int){
        gpsTrackDao.deleteGPSTrack(gpsTrackId)
    }

    suspend fun createGPSTrack(gpsTrackId: Int){
        gpsTrackDao.createGPSTrack(gpsTrackId)
    }
}