package com.app.java.trackingrunningapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.data.repository.GPSTrackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GPSTrackViewModel (
    private val gpsTrackRepository: GPSTrackRepository,
): ViewModel() {

    suspend fun initiateGPSTrack() {
        Log.d("GPS Track View Model", "Initiating session and GPS tracking")
        gpsTrackRepository.createGPSTrack()
    }

    fun resumeGPSTrack() {
        viewModelScope.launch(Dispatchers.IO) {
            gpsTrackRepository.resumeGPSTrack()
        }
    }

    suspend fun stopGPSTrack() {
        viewModelScope.launch(Dispatchers.IO) {
            gpsTrackRepository.stopGPSTrack()
        }
    }
}