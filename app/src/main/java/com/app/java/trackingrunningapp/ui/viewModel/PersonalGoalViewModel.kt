package com.app.java.trackingrunningapp.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.ui.data.repository.PersonalGoalRepository
import com.app.java.trackingrunningapp.ui.data.repository.RunSessionRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PersonalGoalViewModel(
    private val personalGoalRepository: PersonalGoalRepository,
    private val runSessionRepository: RunSessionRepository
): ViewModel() {

    private var updateJob: Job? = null

    fun createPersonalGoal(
        targetDistance: Double?,
        targetDuration: Double?,
        targetCaloriesBurned: Double?,
        frequency: String
    ) {
        viewModelScope.launch {
            personalGoalRepository.createPersonalGoal(
                targetDistance = targetDistance,
                targetDuration = targetDuration,
                targetCaloriesBurned = targetCaloriesBurned,
                frequency = frequency
            )
        }
    }

    fun deletePersonalGoal(goalId: Int) {
        viewModelScope.launch {
            personalGoalRepository.deletePersonalGoal(goalId)
        }
    }

    fun startUpdatingGoalProgress() {
        updateJob = viewModelScope.launch {
            while(isActive) {
                personalGoalRepository.updateGoalProgress()
                delay(5000)
            }
        }
    }

    fun stopUpdatingGoalProgress() {
        updateJob?.cancel()
        updateJob = null
    }

    override fun onCleared() {
        super.onCleared()
        stopUpdatingGoalProgress()
        viewModelScope.launch {
            val currentRunSession = runSessionRepository.getCurrentRunSession()

            if (currentRunSession != null) {
                runSessionRepository.finishRunSession(currentRunSession.sessionId)
            } else {
                println("No current run session was found active to be finished!")
            }
        }
    }

}
