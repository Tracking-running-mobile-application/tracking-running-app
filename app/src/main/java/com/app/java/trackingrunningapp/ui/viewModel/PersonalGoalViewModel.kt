package com.app.java.trackingrunningapp.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.ui.data.repository.PersonalGoalRepository
import kotlinx.coroutines.launch

class PersonalGoalViewModel(
    private val personalGoalRepository: PersonalGoalRepository
): ViewModel() {

    fun createPersonalGoal(
        targetDistance: Float?,
        targetDuration: Float?,
        targetCaloriesBurned: Float?,
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

    fun updateGoalProgress() {
        viewModelScope.launch {
            personalGoalRepository.updateGoalProgress()
        }
    }
}
