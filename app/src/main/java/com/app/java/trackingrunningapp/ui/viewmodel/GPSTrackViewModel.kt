package com.app.java.trackingrunningapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.data.repository.GPSTrackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GPSTrackViewModel (
    private val gpsTrackRepository: GPSTrackRepository,
): ViewModel() {

    fun initiateGPSTrack() {
        viewModelScope.launch(Dispatchers.IO) {
            gpsTrackRepository.createGPSTrack()
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