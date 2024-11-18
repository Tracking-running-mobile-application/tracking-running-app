package com.app.java.trackingrunningapp.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.ui.data.entities.TrainingPlan
import com.app.java.trackingrunningapp.ui.data.repository.NotificationRepository
import com.app.java.trackingrunningapp.ui.data.repository.RunSessionRepository
import com.app.java.trackingrunningapp.ui.data.repository.TrainingPlanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class TrainingPlanViewModel(
    private val trainingPlanRepository: TrainingPlanRepository,
    private val notificationRepository: NotificationRepository,
    private val runSessionRepository: RunSessionRepository
): ViewModel() {

    private val _recommendedPlans = MutableStateFlow<List<TrainingPlan>>(emptyList())
    val recommendedPlans : StateFlow<List<TrainingPlan>> = _recommendedPlans

    init {
        fetchRecommendedPlansDaily()
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
        estimatedTime: Float,
        targetDistance: Float,
        targetDuration: Float,
        targetCaloriesBurned: Float,
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


}