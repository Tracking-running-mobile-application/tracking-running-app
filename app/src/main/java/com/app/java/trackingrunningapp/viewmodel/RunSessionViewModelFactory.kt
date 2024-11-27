package com.app.java.trackingrunningapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.java.trackingrunningapp.model.repositories.GPSPointRepository
import com.app.java.trackingrunningapp.model.repositories.RunSessionRepository

class RunSessionViewModelFactory(
    private val runSessionRepository: RunSessionRepository,
    private val gpsPointRepository: GPSPointRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RunSessionViewModel::class.java)) {
            return RunSessionViewModel(runSessionRepository, gpsPointRepository) as T
        }
        throw IllegalArgumentException ("Unknown ViewModel class ${modelClass.name}")
    }
}