package com.app.java.trackingrunningapp.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.ui.data.entities.TrainingPlan
import com.app.java.trackingrunningapp.ui.data.repositories.NotificationRepository
import com.app.java.trackingrunningapp.ui.data.repositories.RunSessionRepository
import com.app.java.trackingrunningapp.ui.data.repositories.TrainingPlanRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TrainingPlanViewModel(
    private val trainingPlanRepository: TrainingPlanRepository,
    private val notificationRepository: NotificationRepository,
    private val runSessionRepository: RunSessionRepository
): ViewModel() {
    private val _recommendedPlans = MutableStateFlow<List<TrainingPlan>>(emptyList())
    val recommendedPlans : StateFlow<List<TrainingPlan>> = _recommendedPlans

    private val _goalProgress = MutableStateFlow(0.0)
    val goalProgress: StateFlow<Double> = _goalProgress

    init {
        fetchRecommendedPlansDaily()
        observeRunSession()
    }

    fun fetchRecommendedPlansDaily() {
        viewModelScope.launch {
            val plans = trainingPlanRepository.updateTrainingPlanRecommendation()
            if (plans.isNotEmpty()) {
                _recommendedPlans.value = plans

                trainingPlanRepository.updateTrainingPlanRecommendation()
            }
        }
    }

    fun createTrainingPlan(
        title: String,
        description: String,
        estimatedTime: Double,
        targetDistance: Double?,
        targetDuration: Double?,
        targetCaloriesBurned: Double?,
        exerciseType: String,
        difficulty: String
    ) {
        viewModelScope.launch {
            trainingPlanRepository.createTrainingPlan(
                title, description, estimatedTime, targetDistance,
                targetDuration, targetCaloriesBurned, exerciseType, difficulty
            )
        }
    }

    fun deleteTrainingPlan(planId: Int) {
        viewModelScope.launch {
            trainingPlanRepository.deleteTrainingPlan(planId)
        }
    }

    fun initiateTrainingPlan() {
        observeRunSession()
    }

    fun stopActiveTrainingPlan() {
        trainingPlanRepository.stopTrainingPlan()
    }


    private fun observeRunSession() {
        viewModelScope.launch {
            runSessionRepository.currentRunSession.collect { session ->
                if (session == null) {
                    trainingPlanRepository.stopTrainingPlan()
                } else {
                    trainingPlanRepository.startTrainingPlan()
                }
            }
        }
    }
}