package com.app.java.trackingrunningapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.ui.data.entities.RunSession
import com.app.java.trackingrunningapp.ui.data.repository.RunSessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class RunSessionViewModel(private val repository: RunSessionRepository): ViewModel() {
    val startDate = MutableLiveData<String>()
    val endDate = MutableLiveData<String>()
    val filteredSessions = MutableLiveData<Flow<List<RunSession>>>()

    fun filterSessionsByDateRange() {
        val start = startDate.value
        val end = endDate.value
        if (start != null && end != null) {
            viewModelScope.launch {
                val sessions = repository.filterRunningSessionByDay(start, end)
                filteredSessions.value = sessions
            }
        }
    }
}