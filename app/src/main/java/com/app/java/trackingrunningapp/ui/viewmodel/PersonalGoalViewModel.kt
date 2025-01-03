package com.app.java.trackingrunningapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.data.model.entity.PersonalGoal
import com.app.java.trackingrunningapp.data.repository.PersonalGoalRepository
import com.app.java.trackingrunningapp.data.repository.RunSessionRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PersonalGoalViewModel(
    private val personalGoalRepository: PersonalGoalRepository,
    private val runSessionRepository: RunSessionRepository
): ViewModel() {

    private val _personalGoals = MutableStateFlow<List<PersonalGoal>>(emptyList())
    val personalGoalsLiveData = _personalGoals.asLiveData()

    private val _goalProgress = MutableStateFlow<Double?>(null)
    val goalProgress: LiveData<Double?> = _goalProgress.asLiveData()

    private var goalProgressJob: Job? = null

    fun deletePersonalGoal(goalId: Int) {
        viewModelScope.launch {
            personalGoalRepository.deletePersonalGoal(goalId)
            _personalGoals.value = _personalGoals.value.filterNot { it.goalId == goalId }
        }
    }

    suspend fun initiatePersonalGoal() {
        viewModelScope.launch {
            observeRunSession()
            fetchGoalProgress()
            personalGoalRepository.assignSessionToPersonalGoal()
        }
    }

    fun loadPersonalGoals() {
        viewModelScope.launch {
            val personalGoals = personalGoalRepository.getAllPersonalGoals()
            _personalGoals.value = personalGoals
        }
    }

    fun fetchGoalProgress() {
        goalProgressJob?.cancel()
        goalProgressJob = viewModelScope.launch {
            while (isActive) {
                try {
                    val progress = personalGoalRepository.getGoalProgress()
                    _goalProgress.value = progress
                    delay(7000)
                } catch (e: Exception) {
                    println("Error fetching goal progress: ${e.message}")
                }
            }
        }
    }

    fun upsertPersonalGoal(
        goalId: Int? = null,
        sessionId: Int? = null,
        targetDistance: Double? = null,
        targetDuration: Double? = null,
        targetCaloriesBurned: Double? = null
    ) {
        viewModelScope.launch {
            val updatedGoal = personalGoalRepository.upsertPersonalGoal(
                goalId = goalId,
                sessionId = sessionId,
                targetDistance = targetDistance,
                targetDuration = targetDuration,
                targetCaloriesBurned = targetCaloriesBurned,
                existingGoals = _personalGoals.value
            )

            _personalGoals.value = _personalGoals.value.map {
                if (it.goalId == updatedGoal.goalId) updatedGoal else it
            } + if (_personalGoals.value.none { it.goalId == updatedGoal.goalId }) listOf(updatedGoal) else emptyList()
        }
    }

    private fun observeRunSession() {
        viewModelScope.launch {
            runSessionRepository.currentRunSession.collect { session ->
                if (session == null) {
                    personalGoalRepository.updateGoalProgress()
                    val progress = personalGoalRepository.getGoalProgress()
                    _goalProgress.value = progress
                    personalGoalRepository.stopUpdatingGoalProgress()
                    goalProgressJob?.cancel()
                } else {
                    personalGoalRepository.startUpdatingGoalProgress()
                    fetchGoalProgress()
                }
            }
        }
    }

}
