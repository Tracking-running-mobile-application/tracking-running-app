package com.app.java.trackingrunningapp.data.repository

import com.app.java.trackingrunningapp.data.dao2.GPSPointDao
import com.app.java.trackingrunningapp.data.dao2.GPSTrackDao
import com.app.java.trackingrunningapp.data.dao2.RunSessionDao
import com.app.java.trackingrunningapp.data.model.entity.gps.GPSPoint
import com.app.java.trackingrunningapp.data.model.entity.goal.RunSession
import com.app.java.trackingrunningapp.data.model.dataclass.location.Location
import com.app.java.trackingrunningapp.utils.DateTimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.IllegalStateException

class GPSPointRepository(
    private val gpsPointDao: GPSPointDao
) {
    suspend fun insertGPSPoint(gpsPoint: GPSPoint){
        gpsPointDao.insertGPSPoint(gpsPoint)
    }
    suspend fun getTwoLatestLocation(trackId: Int){
        gpsPointDao.getTwoLatestLocation(trackId)
    }
}