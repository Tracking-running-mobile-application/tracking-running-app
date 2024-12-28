package com.app.java.trackingrunningapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.app.java.trackingrunningapp.data.repository.GPSPointRepository

class GPSPointViewModel(
    private val gpsPointRepository: GPSPointRepository
): ViewModel() {

    suspend fun insertGPSPoint(
        longitude: Double,
        latitude: Double
    ) {
        gpsPointRepository.insertGPSPoint(
            longitude = longitude,
            latitude = latitude
        )
    }

}
