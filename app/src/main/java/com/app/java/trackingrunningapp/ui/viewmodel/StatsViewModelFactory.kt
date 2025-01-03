package com.app.java.trackingrunningapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.java.trackingrunningapp.data.repository.StatsRepository

class StatsViewModelFactory(
    private val statsRepository: StatsRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatsViewModel::class.java)) {
            return StatsViewModel(statsRepository) as T
        }
        throw IllegalArgumentException ("Unknown ViewModel class ${modelClass.name}")
    }
}