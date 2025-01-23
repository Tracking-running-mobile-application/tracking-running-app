package com.app.java.trackingrunningapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.java.trackingrunningapp.data.repository.GPSTrackRepository

class GPSTrackViewModelFactory(
    private val gpsTrackRepository: GPSTrackRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GPSTrackViewModel::class.java)) {
            return GPSTrackViewModel(gpsTrackRepository) as T
        }
        throw IllegalArgumentException ("Unknown ViewModel class ${modelClass.name}")
    }
}