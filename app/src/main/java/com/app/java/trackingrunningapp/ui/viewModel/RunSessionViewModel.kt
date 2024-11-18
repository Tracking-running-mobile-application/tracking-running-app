package com.app.java.trackingrunningapp.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.ui.data.entities.RunSession
import com.app.java.trackingrunningapp.ui.data.repository.RunSessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RunSessionViewModel(
    private val runSessionRepository: RunSessionRepository
): ViewModel() {
    val startDate = MutableLiveData<String>()
    val endDate = MutableLiveData<String>()


    private val _filteredSessions = MutableStateFlow<List<RunSession>>(emptyList())
    val filteredSession: StateFlow<List<RunSession>> = _filteredSessions

    private val _runSessions = MutableStateFlow<List<RunSession>>(emptyList())
    val runSessions: StateFlow<List<RunSession>> = _runSessions

    private val _currentRunSession = MutableStateFlow<RunSession?>(null)
    val currentRunSession : StateFlow<RunSession?> = _currentRunSession

    init {
        getAllRunSessions()
    }

    fun filterSessionsByDateRange() {
        val start = startDate.value
        val end = endDate.value
        if (start != null && end != null) {
            viewModelScope.launch {
                runSessionRepository.filterRunningSessionByDay(start, end)
                    .collect { sessions ->
                        _filteredSessions.value = sessions
                    }
            }
        } else {
            println("Either or both the start date and end date are missing!")
        }
    }

    fun getAllRunSessions() {
        viewModelScope.launch {
            runSessionRepository.getAllRunSessions()
                .collect { sessions ->
                    _runSessions.value = sessions
                }
        }
    }

    fun getCurrentRunSession() {
        viewModelScope.launch {
            val session = runSessionRepository.getCurrentRunSession()
            _currentRunSession.value = session
        }
    }

    fun updateRunSession(
        distance: Float,
        duration: Float,
        caloriesBurned: Float,
    ) {
        viewModelScope.launch {
            runSessionRepository.updateRunSession(
                distance = distance,
                duration = duration,
                caloriesBurned = caloriesBurned
            )
        }
    }

    fun startRunSession(sessionId: Int) {
        viewModelScope.launch {
            runSessionRepository.startRunSession(sessionId)
        }
    }

    fun finishRunSession(sessionId: Int) {
        viewModelScope.launch {
            runSessionRepository.finishRunSession(sessionId)
        }
    }

}