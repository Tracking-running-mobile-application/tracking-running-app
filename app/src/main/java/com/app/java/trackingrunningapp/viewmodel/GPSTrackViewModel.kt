package com.app.java.trackingrunningapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.model.repositories.GPSPointRepository
import com.app.java.trackingrunningapp.model.repositories.GPSTrackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GPSTrackViewModel (
    private val gpsTrackRepository: GPSTrackRepository,
    private val gpsPointRepository: GPSPointRepository
): ViewModel() {

    fun initiateGPSTrack() {
        viewModelScope.launch(Dispatchers.IO) {
            gpsTrackRepository.initiateGPSTrack()
        }
    }

    fun startGPSTrack() {
        viewModelScope.launch(Dispatchers.IO) {
            gpsTrackRepository.startGPSTrack()
        }
    }

    fun stopGPSTrack() {
        viewModelScope.launch(Dispatchers.IO) {
            gpsTrackRepository.stopGPSTrack()
        }
    }
}