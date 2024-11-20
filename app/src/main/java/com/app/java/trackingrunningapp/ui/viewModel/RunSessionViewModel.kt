package com.app.java.trackingrunningapp.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.ui.data.entities.RunSession
import com.app.java.trackingrunningapp.ui.data.repository.PersonalGoalRepository
import com.app.java.trackingrunningapp.ui.data.repository.RunSessionRepository
import com.app.java.trackingrunningapp.ui.data.repository.TrainingPlanRepository
import com.app.java.trackingrunningapp.ui.utils.DateTimeUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class RunSessionViewModel(
    private val runSessionRepository: RunSessionRepository,
    private val trainingPlanRepository: TrainingPlanRepository,
    private val personalGoalRepository: PersonalGoalRepository
): ViewModel() {
    val startDate = MutableLiveData<String>()
    val endDate = MutableLiveData<String>()

    private val _filteredSessions = MutableStateFlow<List<RunSession>>(emptyList())
    val filteredSession: StateFlow<List<RunSession>> = _filteredSessions

    private val _runSessions = MutableStateFlow<List<RunSession>>(emptyList())
    val runSessions: StateFlow<List<RunSession>> = _runSessions

    private val _duration = MutableStateFlow("00:00")
    val duration: StateFlow<String> = _duration

    private val _distance = MutableStateFlow(0.0)
    val distance: StateFlow<Double> = _distance

    private val _pace = MutableStateFlow(0.0)
    val pace: StateFlow<Double> = _pace

    private val _caloriesBurned = MutableStateFlow(0.0)
    val caloriesBurned: StateFlow<Double> = _caloriesBurned

    private val _hasMoreData = MutableStateFlow(true)
    val hasMoreData: StateFlow<Boolean> = _hasMoreData

    private val _favoriteRunSessions = MutableStateFlow<List<RunSession?>>(emptyList())
    val favoriteRunSessions : StateFlow<List<RunSession?>> = _favoriteRunSessions

    private val currentRunSession: StateFlow<RunSession?> = runSessionRepository.currentRunSession

    private var runSessionStartTime: Instant = Instant.DISTANT_PAST

    private var trackingJob: Job? = null

    init {
        fetchRunSessions()
        loadFavoriteSessions()
    }

    private fun getCurrentSessionOrThrow(): RunSession {
        return currentRunSession.value ?: throw IllegalStateException("Value of current run session is null!")
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

    fun fetchRunSessions(fetchMore: Boolean = false) {
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

    fun startRunSession() {
        runSessionStartTime = DateTimeUtils.getCurrentInstant()

        viewModelScope.launch {
            try {
                runSessionRepository.startRunSession()
            } catch(e: Exception) {
                println("Error starting session: ${e.message}")
            }
        }

        trackingJob?.cancel()
        trackingJob = viewModelScope.launch {
            while (isActive) {
                val currentTime = DateTimeUtils.getCurrentInstant()
                val formattedDuration = DateTimeUtils.calculateDuration(runSessionStartTime, currentTime)
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

            val durationInSeconds = DateTimeUtils.durationToSeconds(_duration.value)

            runSessionRepository.updateDurationSession(durationInSeconds)
            runSessionRepository.updatePaceRunSession()

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

    private suspend fun updateCaloriesAndPace() {
        val newPace = runSessionRepository.updatePaceRunSession()
        _pace.value = newPace

        val newCaloriesBurned = runSessionRepository.updateCaloriesBurnedSession()
        _caloriesBurned.value = newCaloriesBurned

        runSessionRepository.updateDistanceSession()
    }

}