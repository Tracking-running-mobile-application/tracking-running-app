package com.app.java.trackingrunningapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.java.trackingrunningapp.data.repository.GPSPointRepository
import com.app.java.trackingrunningapp.data.repository.RunSessionRepository

class RunSessionViewModelFactory(
    private val runSessionRepository: RunSessionRepository,
    private val gpsPointRepository: GPSPointRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RunSessionViewModel::class.java)) {
            return RunSessionViewModel(runSessionRepository) as T
        }
        throw IllegalArgumentException ("Unknown ViewModel class ${modelClass.name}")
    }
}