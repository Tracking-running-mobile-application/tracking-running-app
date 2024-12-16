package com.app.java.trackingrunningapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.data.model.entity.goal.RunSession
import com.app.java.trackingrunningapp.data.repository.RunSessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RunSessionViewModel(
    private val runSessionRepository: RunSessionRepository
) : ViewModel() {
    private val _filteredSessions = MutableLiveData<List<RunSession>>()
    val filteredSession:LiveData<List<RunSession>> = _filteredSessions

    private val _runSessions = MutableLiveData<List<RunSession>>()
    val runSessions:LiveData<List<RunSession>> = _runSessions

    private val _hasMoreData = MutableStateFlow(true)
    val hasMoreData: StateFlow<Boolean> = _hasMoreData

    private val _favoriteRunSessions = MutableLiveData<List<RunSession?>>()
    val favoriteRunSessions: LiveData<List<RunSession?>> = _favoriteRunSessions


    fun deleteRunSession(session: RunSession) {
        viewModelScope.launch(Dispatchers.IO) {
            runSessionRepository.deleteRunSession(session)
        }
    }

}