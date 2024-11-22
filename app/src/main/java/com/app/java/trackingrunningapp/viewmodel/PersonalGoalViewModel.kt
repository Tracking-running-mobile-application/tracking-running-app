package com.app.java.trackingrunningapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.model.entities.PersonalGoal
import com.app.java.trackingrunningapp.model.repositories.PersonalGoalRepository
import com.app.java.trackingrunningapp.model.repositories.RunSessionRepository
import com.app.java.trackingrunningapp.ui.utils.DateTimeUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PersonalGoalViewModel(
    private val personalGoalRepository: PersonalGoalRepository,
    private val runSessionRepository: RunSessionRepository
): ViewModel() {

    private val _personalGoals = MutableStateFlow<List<PersonalGoal>>(emptyList())
    val personalGoals: StateFlow<List<PersonalGoal>> = _personalGoals

    init {
        loadPersonalGoals()
    }

    fun deletePersonalGoal(goalId: Int) {
        viewModelScope.launch {
            personalGoalRepository.deletePersonalGoal(goalId)
            _personalGoals.value = _personalGoals.value.filterNot { it.goalId == goalId }
        }
    }

    fun initiatePersonalGoal() {
        observeRunSession()
    }

    fun loadPersonalGoals() {
        viewModelScope.launch {
            val personalGoals = personalGoalRepository.getAllPersonalGoals()
            _personalGoals.value = personalGoals
        }
    }

    fun upsertPersonalGoal(
        goalId: Int? = null,
        sessionId : Int? = null,
        targetDistance: Double?,
        targetDuration: Double?,
        targetCaloriesBurned: Double?,
        frequency: String
    ) {
        viewModelScope.launch {
            val existingGoal = if (goalId != null) {
                _personalGoals.value.find { it.goalId == goalId}
            } else null

            val currentDateString = DateTimeUtils.getCurrentDate().toString()

            val personalGoal = existingGoal?.copy (
                goalSessionId = sessionId ?: existingGoal.goalSessionId,
                targetDistance = targetDistance ?: existingGoal.targetDistance,
                targetDuration = targetDuration ?: existingGoal.targetDuration,
                targetCaloriesBurned = targetCaloriesBurned ?: existingGoal.targetCaloriesBurned,
                frequency = frequency,
                dateCreated = currentDateString
            )
                ?: PersonalGoal(
                    goalId = 0,
                    goalSessionId = sessionId,
                    targetDistance = targetDistance,
                    targetDuration = targetDuration,
                    targetCaloriesBurned = targetCaloriesBurned,
                    frequency = frequency,
                    dateCreated = currentDateString
                )

            personalGoalRepository.upsertPersonalGoal(personalGoal)

            _personalGoals.value = _personalGoals.value.map {
                if (it.goalId == personalGoal.goalId) personalGoal else it
            } + if (existingGoal == null) listOf(personalGoal) else emptyList()
        }
    }

    private fun observeRunSession() {
        viewModelScope.launch {
            runSessionRepository.currentRunSession.collect { session ->
                if (session == null) {
                    personalGoalRepository.stopPersonalGoal()
                } else {
                    personalGoalRepository.startPersonalGoal()
                }
            }
        }
    }

}
