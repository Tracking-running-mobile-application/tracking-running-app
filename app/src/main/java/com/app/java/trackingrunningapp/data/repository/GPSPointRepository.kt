package com.app.java.trackingrunningapp.data.repository

import com.app.java.trackingrunningapp.data.dao.GPSPointDao
import com.app.java.trackingrunningapp.data.model.entity.gps.GPSPoint

class GPSPointRepository(
    private val gpsPointDao: GPSPointDao
) {
    suspend fun insertGPSPoint(gpsPoint: GPSPoint){
        gpsPointDao.insertGPSPoint(gpsPoint)
    }
//    suspend fun getTwoLatestLocation(trackId: Int){
//        gpsPointDao.getTwoLatestLocation(trackId)
//    }
}