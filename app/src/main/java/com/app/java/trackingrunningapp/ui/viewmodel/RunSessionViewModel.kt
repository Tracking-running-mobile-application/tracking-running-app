package com.app.java.trackingrunningapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.data.model.entity.RunSession
import com.app.java.trackingrunningapp.data.model.dataclass.location.StatsSession
import com.app.java.trackingrunningapp.data.repository.RunSessionRepository
import com.app.java.trackingrunningapp.utils.StatsUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class RunSessionViewModel(
    private val runSessionRepository: RunSessionRepository,
): ViewModel() {
    private val _filteredSessions = MutableStateFlow<List<RunSession>>(emptyList())
    val filteredSession: StateFlow<List<RunSession>> = _filteredSessions

    private val _runSessions = MutableStateFlow<List<RunSession>>(emptyList())
    val runSessions: StateFlow<List<RunSession>> = _runSessions

    private val _hasMoreData = MutableStateFlow(true)
    val hasMoreData: StateFlow<Boolean> = _hasMoreData

    private val _favoriteRunSessions = MutableStateFlow<List<RunSession?>>(emptyList())
    val favoriteRunSessions : StateFlow<List<RunSession?>> = _favoriteRunSessions

    val currentSession = runSessionRepository.currentRunSession

    private val _statsFlow = MutableStateFlow<StatsSession?>(null)
    val statsFlow: StateFlow<StatsSession?> = _statsFlow

    private var statsUpdateJob: Job? = null

    init {
        fetchRunSessions()
        loadFavoriteSessions()
    }

    fun filterSessionsByDateRange(startDate: String, endDate: String) {
        viewModelScope.launch {
            try {
                val sessions = runSessionRepository.filterRunningSessionByDay(startDate, endDate)
                _filteredSessions.value = sessions
            } catch(e: Exception) {
                println("Error filtering sessions: ${e.message}")
            }
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

    suspend fun initiateRunSession() {
        withContext(Dispatchers.IO) {
            try {
                runSessionRepository.startRunSession()
            } catch(e: Exception) {
                println("Error starting session: ${e.message}")
            }
        }
    }

    suspend fun startStatsUpdate() {
        statsUpdateJob?.cancelAndJoin()
        statsUpdateJob = null
        statsUpdateJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    updateStats()
                    delay(5000)
                } catch (e: Exception) {
                    println("Error updating stats: ${e.message}")
                }
            }
        }
    }

    suspend fun pauseRunSession() {
        statsUpdateJob?.cancelAndJoin()
        statsUpdateJob = null
        Log.d("StatsUpdate", "Stats update paused")
    }

    suspend fun resumeRunSession() {
        startStatsUpdate()
        fetchStatsCurrentSession()
        Log.d("StatsUpdate", "Stats update resumed")
    }

    fun finishRunSession() {
        viewModelScope.launch(Dispatchers.IO) {
            statsUpdateJob?.cancelAndJoin()
            statsUpdateJob = null
            runSessionRepository.setRunSessionInactive()
            Log.d("StatsUpdate", "Stats update finished in finishRunSession")
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

    suspend fun fetchStatsCurrentSession() {
        statsUpdateJob?.cancelAndJoin()
        statsUpdateJob = null
        statsUpdateJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    val stats = runSessionRepository.fetchStatsSession()
                    _statsFlow.emit(stats)
                    delay(5000)
                } catch (e: CancellationException) {
                    Log.d("StatsUpdate", "Job canceled during execution ${e.message}")
                    throw e
                } catch (e: Exception) {
                    println("Error updating stats: ${e.message}")
                }
            }
        }
    }

    suspend fun updateStats() {
        statsUpdateJob?.cancelAndJoin()
        statsUpdateJob = null
        statsUpdateJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    runSessionRepository.calcDuration()
                    runSessionRepository.calcPace()
                    runSessionRepository.calcCaloriesBurned()
                    runSessionRepository.calcDistance()
                    val newPace = runSessionRepository.pace.value

                    val newCaloriesBurned = runSessionRepository.caloriesBurned.value

                    val newDistance = runSessionRepository.distance.value
                    val newDuration =
                        StatsUtils.durationToSeconds(runSessionRepository.duration.value)

                    runSessionRepository.updateStatsSession(
                        newDistance,
                        newDuration,
                        newCaloriesBurned,
                        newPace
                    )

                    delay(1200)
                } catch (e: CancellationException) {
                    Log.d("StatsUpdate", "Job canceled during execution")
                    throw e
                }
            }
            Log.d("StatsUpdate", "Coroutine completed in updateStats")
        }
    }

    fun deleteRunSession(sessionId: Int) {
        viewModelScope.launch {
            runSessionRepository.deleteRunSession(sessionId)
        }
    }

}