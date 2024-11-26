package com.app.java.trackingrunningapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.model.entities.TrainingPlan
import com.app.java.trackingrunningapp.model.repositories.NotificationRepository
import com.app.java.trackingrunningapp.model.repositories.RunSessionRepository
import com.app.java.trackingrunningapp.model.repositories.TrainingPlanRepository
import com.app.java.trackingrunningapp.ui.utils.DateTimeUtils
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

    val goalProgress: StateFlow<Double?> = trainingPlanRepository.goalProgress

    init {
        fetchRecommendedPlansDaily()
    }

    fun fetchRecommendedPlansDaily() {
        viewModelScope.launch {
            val plans = trainingPlanRepository.updateTrainingPlanRecommendation()
            if (plans.isNotEmpty()) {
                _recommendedPlans.value = plans

                plans.forEach { plan ->
                    val today = DateTimeUtils.getCurrentDate().toString()
                    trainingPlanRepository.assignLastDateToTrainingPlan(plan.planId, today)
                }
            }
        }
    }

    fun deleteTrainingPlan(planId: Int) {
        viewModelScope.launch {
            trainingPlanRepository.deleteTrainingPlan(planId)
        }
    }

    /*trigger runSession start before this!!*/
    fun initiateTrainingPlan(planId: Int) {
        observeRunSession()
        viewModelScope.launch {
            val currentSession = runSessionRepository.currentRunSession.value
            if (currentSession != null) {
                trainingPlanRepository.assignSessionToTrainingPlan(planId, currentSession.sessionId)
            }
            else {
               println("No active current session found!")
            }
        }
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