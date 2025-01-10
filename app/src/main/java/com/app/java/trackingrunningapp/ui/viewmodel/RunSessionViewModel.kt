package com.app.java.trackingrunningapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.data.model.entity.RunSession
import com.app.java.trackingrunningapp.data.model.dataclass.location.StatsSession
import com.app.java.trackingrunningapp.data.repository.RunSessionRepository
import com.app.java.trackingrunningapp.utils.StatsUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class RunSessionViewModel(
    private val runSessionRepository: RunSessionRepository,
): ViewModel() {
    private val _filteredSessions = MutableStateFlow<List<RunSession>>(emptyList())
    val filteredSession = _filteredSessions.asLiveData()

    private val _runSessions = MutableStateFlow<List<RunSession>>(emptyList())
    val runSessions = _runSessions.asLiveData()

    private val _hasMoreData = MutableStateFlow(true)
    val hasMoreData = _hasMoreData.asLiveData()

    private val _favoriteRunSessions = MutableStateFlow<List<RunSession?>>(emptyList())
    val favoriteRunSessions : StateFlow<List<RunSession?>> = _favoriteRunSessions

    val currentSession = runSessionRepository.currentRunSession

    private val _statsFlow = MutableStateFlow<StatsSession?>(null)
    val statsFlow= _statsFlow.asLiveData()

    private var statsUpdateJob: Job? = null
    private var fetchStatsJob: Job? = null

    private var jobMutex = Mutex()

    init {
        fetchRunSessions()
        loadFavoriteSessions()
    }

    fun filterSessionsByDateRange(startDate: String, endDate: String) {
        viewModelScope.launch {
            try {
                val sessions= runSessionRepository.filterRunningSessionByDay(startDate, endDate)
                _filteredSessions.value = sessions
            } catch(e: Exception) {
                println("Error filtering sessions: ${e.message}")
            }
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

    fun reloadRunSessionHistory() {
        viewModelScope.launch {
            _runSessions.value = runSessionRepository.refreshRunSessionHistory()
        }
    }

    suspend fun initiateRunSession() {
        try {
            runSessionRepository.resetStatsValue()
            runSessionRepository.startRunSession()
        } catch(e: Exception) {
            println("Error starting session: ${e.message}")
        }
    }

    fun setRunSessionStartTime() {
        viewModelScope.launch(Dispatchers.IO) {
            runSessionRepository.setRunSessionStartTime()
        }
    }

    suspend fun pauseRunSession() {
        statsUpdateJob?.cancelAndJoin()
        fetchStatsJob?.cancelAndJoin()
        runSessionRepository.stopUpdatingStats()
    }

    suspend fun fetchAndUpdateStats() = withContext(Dispatchers.IO){
        Log.d("RunPlanFragment Stop", "fetchAndUpdate")
        updateStats()
        fetchStatsCurrentSession()
    }

    fun finishRunSession() {
        CoroutineScope(Dispatchers.IO).launch {
            jobMutex.withLock {
                Log.d("RunPlanFragment Stop", "Do something in VM")
                runSessionRepository.stopUpdatingStats()
                statsUpdateJob?.cancelAndJoin()
                fetchStatsJob?.cancelAndJoin()
                runSessionRepository.resetStatsValue()
                runSessionRepository.setRunSessionInactive()
                Log.d("StatsUpdate", "Stats update finished in finishRunSession")
            }
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

    private suspend fun fetchStatsCurrentSession() {
        fetchStatsJob?.cancelAndJoin()
        fetchStatsJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    val stats = runSessionRepository.fetchStatsSession()
                    _statsFlow.emit(stats)
                    delay(1000)
                } catch (e: CancellationException) {
                    Log.d("fetchStatsCurrentSession()", "Job canceled during execution ${e.message}")
                    throw e
                } catch (e: Exception) {
                    println("Error updating stats: ${e.message}")
                }
            }
        }
    }

    private suspend fun updateStats() {
        statsUpdateJob?.cancelAndJoin()
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
                    val newDuration = runSessionRepository.duration.value

                    runSessionRepository.updateStatsSession(
                        newDistance,
                        newDuration,
                        newCaloriesBurned,
                        newPace
                    )

                    delay(1000)
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