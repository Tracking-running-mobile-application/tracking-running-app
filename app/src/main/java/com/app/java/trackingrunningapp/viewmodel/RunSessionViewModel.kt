package com.app.java.trackingrunningapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.model.entities.RunSession
import com.app.java.trackingrunningapp.model.models.StatsSession
import com.app.java.trackingrunningapp.model.repositories.GPSPointRepository
import com.app.java.trackingrunningapp.model.repositories.GPSTrackRepository
import com.app.java.trackingrunningapp.model.repositories.RunSessionRepository
import com.app.java.trackingrunningapp.model.utils.StatsUtils
import com.app.java.trackingrunningapp.ui.utils.DateTimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlin.random.Random

class RunSessionViewModel(
    private val runSessionRepository: RunSessionRepository,
    private val gpsPointRepository: GPSPointRepository
): ViewModel() {
    val startDate = MutableLiveData<String>()
    val endDate = MutableLiveData<String>()

    private val _filteredSessions = MutableStateFlow<List<RunSession>>(emptyList())
    val filteredSession: StateFlow<List<RunSession>> = _filteredSessions

    private val _runSessions = MutableStateFlow<List<RunSession>>(emptyList())
    val runSessions: StateFlow<List<RunSession>> = _runSessions

    private val _duration = MutableStateFlow("00:00")
    val duration: StateFlow<String> = _duration

    val distance = gpsPointRepository.currentDistance

    private val _pace = MutableStateFlow(0.0)
    val pace: StateFlow<Double> = _pace

    private val _caloriesBurned = MutableStateFlow(0.0)
    val caloriesBurned: StateFlow<Double> = _caloriesBurned

    private val _hasMoreData = MutableStateFlow(true)
    val hasMoreData: StateFlow<Boolean> = _hasMoreData

    private val _favoriteRunSessions = MutableStateFlow<List<RunSession?>>(emptyList())
    val favoriteRunSessions : StateFlow<List<RunSession?>> = _favoriteRunSessions

    private var runSessionStartTime: Instant = Instant.DISTANT_PAST

    private var trackingJob: Job? = null

    val currentSession = runSessionRepository.currentRunSession

    init {
        fetchRunSessions()
        loadFavoriteSessions()
    }

    fun filterSessionsByDateRange() {
        val start = startDate.value
        val end = endDate.value
        if (start != null && end != null) {
            viewModelScope.launch {
                try {
                    val sessions = runSessionRepository.filterRunningSessionByDay(start, end)
                    _filteredSessions.value = sessions
                } catch(e: Exception) {
                    println("Error filtering sessions: ${e.message}")
                }
            }
        } else {
            println("Either or both the start date and end date are missing!")
        }
    }

    private fun fetchRunSessions(fetchMore: Boolean = false) {
        viewModelScope.launch {
            val (newSessions, hasMore) = runSessionRepository.getAllRunSessions(fetchMore)

            if (fetchMore) {
                _runSessions.value = _runSessions.value + newSessions
            } else {
                _runSessions.value = newSessions
            }

            _hasMoreData.value = hasMore
        }
    }

    fun reloadRunSessionHistory() {
        viewModelScope.launch {
            _runSessions.value = runSessionRepository.refreshRunSessionHistory()
        }
    }

    fun startRunSession() {
        runSessionStartTime = DateTimeUtils.getCurrentInstant()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                runSessionRepository.startRunSession()
                /*Is it optimal to place it here?*/
                gpsPointRepository.calculateDistance()
            } catch(e: Exception) {
                println("Error starting session: ${e.message}")
            }
        }

        trackingJob?.cancel()
        trackingJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                val currentTime = DateTimeUtils.getCurrentInstant()
                val formattedDuration = StatsUtils.calculateDuration(runSessionStartTime, currentTime)
                _duration.value = formattedDuration

                if (currentTime.epochSeconds % 4 == 0L) {
                    updateCaloriesAndPace()
                }

                delay(1000)
            }
        }

    }

    fun finishRunSession() {
        trackingJob?.cancel()
        trackingJob = null

        viewModelScope.launch {
            updateCaloriesAndPace()

            val durationInSeconds = StatsUtils.durationToSeconds(_duration.value)

            runSessionRepository.updateDurationSession(durationInSeconds)
            runSessionRepository.updatePaceRunSession()
            runSessionRepository.updateDistanceSession(distance.value)

            runSessionRepository.finishRunSession()
        }
    }

    fun addAndRemoveFavoriteSession(session: RunSession) {
        viewModelScope.launch {
            if (session.isFavorite) {
                runSessionRepository.removeFavoriteRunSession(session.sessionId)
                _favoriteRunSessions.value = _favoriteRunSessions.value - session
            } else {
                runSessionRepository.addFavoriteRunSession(session.sessionId)
                _favoriteRunSessions.value = _favoriteRunSessions.value + session
            }
            session.isFavorite = !session.isFavorite
        }
    }

    fun loadFavoriteSessions() {
        viewModelScope.launch {
            val favorites = runSessionRepository.getFavoriteRunSessions()
            _favoriteRunSessions.value = favorites
        }
    }

    suspend fun fetchStatsCurrentSession(): StatsSession {
        return runSessionRepository.fetchStatsSession()
    }

    private suspend fun updateCaloriesAndPace() {
        val newPace = runSessionRepository.updatePaceRunSession()
        _pace.value = newPace

        val newCaloriesBurned = runSessionRepository.updateCaloriesBurnedSession()
        _caloriesBurned.value = newCaloriesBurned

    }

    fun deleteRunSession(sessionId: Int) {
        viewModelScope.launch {
            runSessionRepository.deleteRunSession(sessionId)
        }
    }

}