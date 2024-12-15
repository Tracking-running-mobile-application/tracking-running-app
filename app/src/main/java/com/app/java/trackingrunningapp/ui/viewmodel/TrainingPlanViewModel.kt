package com.app.java.trackingrunningapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.data.model.entity.TrainingPlan
import com.app.java.trackingrunningapp.data.repository.NotificationRepository
import com.app.java.trackingrunningapp.data.repository.RunSessionRepository
import com.app.java.trackingrunningapp.data.repository.TrainingPlanRepository
import com.app.java.trackingrunningapp.utils.DateTimeUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TrainingPlanViewModel(
    private val trainingPlanRepository: TrainingPlanRepository,
    private val notificationRepository: NotificationRepository,
    private val runSessionRepository: RunSessionRepository
): ViewModel() {
    private val _recommendedPlans = MutableStateFlow<List<TrainingPlan>>(emptyList())

    val recommendedPlansLiveData: LiveData<List<TrainingPlan>> = _recommendedPlans.asLiveData()

    private val _goalProgress = MutableStateFlow<Double?>(null)
    val goalProgress: LiveData<Double?> = _goalProgress.asLiveData()

    private var goalProgressJob: Job? = null

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

    fun fetchGoalProgress() {
        goalProgressJob?.cancel()
        goalProgressJob = viewModelScope.launch {
            while (isActive) {
                try {
                    val progress = trainingPlanRepository.getGoalProgress()
                    _goalProgress.value = progress
                    delay(7000)
                } catch (e: Exception) {
                    println("Error fetching goal progress: ${e.message}")
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
    fun initiateTrainingPlan() {
        viewModelScope.launch {
            observeRunSession()
            fetchGoalProgress()
            trainingPlanRepository.assignSessionToTrainingPlan()
        }
    }

    private fun observeRunSession() {
        viewModelScope.launch {
            runSessionRepository.currentRunSession.collect { session ->
                if (session == null) {
                    trainingPlanRepository.updateGoalProgress()
                    val progress = trainingPlanRepository.getGoalProgress()
                    _goalProgress.value = progress
                    trainingPlanRepository.stopUpdatingGoalProgress()
                    goalProgressJob?.cancel()
                } else {
                    trainingPlanRepository.startUpdatingGoalProgress()
                    fetchGoalProgress()
                }
            }
        }
    }
}