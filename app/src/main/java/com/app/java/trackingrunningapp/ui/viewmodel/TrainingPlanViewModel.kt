package com.app.java.trackingrunningapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.app.java.trackingrunningapp.data.model.entity.TrainingPlan
import com.app.java.trackingrunningapp.data.repository.RunSessionRepository
import com.app.java.trackingrunningapp.data.repository.TrainingPlanRepository
import com.app.java.trackingrunningapp.model.repositories.NotificationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
    private val _recommendedPlansBeginner = MutableStateFlow<List<TrainingPlan>>(emptyList())
    private val _recommendedPlansIntermediate = MutableStateFlow<List<TrainingPlan>>(emptyList())
    private val _recommendedPlansAdvanced = MutableStateFlow<List<TrainingPlan>>(emptyList())

    val recommendedPlansBeginner: LiveData<List<TrainingPlan>> = _recommendedPlansBeginner.asLiveData()
    val recommendedPlansIntermediate: LiveData<List<TrainingPlan>> = _recommendedPlansIntermediate.asLiveData()
    val recommendedPlansAdvanced: LiveData<List<TrainingPlan>> = _recommendedPlansAdvanced.asLiveData()

    private val _goalProgress = MutableStateFlow<Double?>(null)
    val goalProgress: LiveData<Double?> = _goalProgress.asLiveData()

    private var goalProgressJob: Job? = null

    fun fetchRecommendedPlans() {
        viewModelScope.launch {
            val beginnerPlans = trainingPlanRepository.getTrainingPlanByDifficulty(exerciseType = "Beginner")
            if (beginnerPlans.isNotEmpty()) {
                _recommendedPlansBeginner.value = beginnerPlans
            }

            val intermediatePlans = trainingPlanRepository.getTrainingPlanByDifficulty(exerciseType = "Intermediate")
            if (beginnerPlans.isNotEmpty()) {
                _recommendedPlansIntermediate.value = intermediatePlans
            }

            val advancedPlans = trainingPlanRepository.getTrainingPlanByDifficulty(exerciseType = "Advanced")
            if (beginnerPlans.isNotEmpty()) {
                _recommendedPlansAdvanced.value = advancedPlans
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
                    delay(1500)
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

    fun stopUpdatingFetchingProgress() {
        goalProgressJob?.cancel()
        trainingPlanRepository.stopUpdatingGoalProgress()
    }

    /*trigger runSession start before this!!*/
    fun initiateTrainingPlan() {
        CoroutineScope(Dispatchers.IO).launch {
            trainingPlanRepository.assignSessionToTrainingPlan()
        }
    }

    fun fetchAndUpdateGoalProgress() {
        Log.d("ObserveRunSession", "1")
        goalProgressJob?.cancel()
        goalProgressJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                try {
                    trainingPlanRepository.startUpdatingGoalProgress()
                    fetchGoalProgress()
                } catch (e: Exception) {
                    println("Error updating and fetching goal: ${e.message}")
                }
            }
        }
    }
}