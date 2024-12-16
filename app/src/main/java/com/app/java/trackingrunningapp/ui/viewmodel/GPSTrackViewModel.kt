package com.app.java.trackingrunningapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.data.model.entity.gps.GPSTrack
import com.app.java.trackingrunningapp.data.repository.GPSTrackRepository
import com.app.java.trackingrunningapp.ui.statistic.StatisticViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GPSTrackViewModel(
    private val repository: GPSTrackRepository
) : ViewModel() {
    fun setGPSTrackInactive(gpsTrackId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setGPSTrackInactive(gpsTrackId)
        }
    }
    fun setGPSTrackActive(gpsTrackId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setGPSTrackActive(gpsTrackId)
        }
    }
    fun getPauseOrContinueGPSTrack(gpsTrackId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getPauseOrContinueGPSTrack(gpsTrackId)
        }
    }
    fun deleteGPSTrack(gpsTrack: GPSTrack) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteGPSTrack(gpsTrack)
        }
    }
    fun createGPSTrack(gpsTrack: GPSTrack) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createGPSTrack(gpsTrack)
        }
    }
    class Factory(
        private val repository: GPSTrackRepository
    ):ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StatisticViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GPSTrackViewModel(repository) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel")
            }
        }
    }
}