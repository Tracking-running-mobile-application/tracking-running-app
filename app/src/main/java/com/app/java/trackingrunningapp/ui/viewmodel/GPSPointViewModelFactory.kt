package com.app.java.trackingrunningapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.java.trackingrunningapp.data.repository.GPSPointRepository
import com.app.java.trackingrunningapp.data.repository.GPSTrackRepository

class GPSPointViewModelFactory (
    private val gpsPointRepository: GPSPointRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GPSPointViewModel::class.java)) {
            return GPSPointViewModel(gpsPointRepository) as T
        }
        throw IllegalArgumentException ("Unknown ViewModel class ${modelClass.name}")
    }
}