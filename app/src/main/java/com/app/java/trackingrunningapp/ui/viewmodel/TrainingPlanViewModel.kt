package com.app.java.trackingrunningapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.java.trackingrunningapp.data.model.entity.TrainingPlan
import com.app.java.trackingrunningapp.data.repository.NotificationRepository
import com.app.java.trackingrunningapp.data.repository.RunSessionRepository
import com.app.java.trackingrunningapp.data.repository.TrainingPlanRepository
import com.app.java.trackingrunningapp.ui.intro.UserViewModel

class TrainingPlanViewModel(
    private val trainingPlanRepository: TrainingPlanRepository,
    private val notificationRepository: NotificationRepository,
    private val runSessionRepository: RunSessionRepository
) : ViewModel() {
    private val _recommendedPlans = MutableLiveData<List<TrainingPlan>>()
    val recommendedPlans: LiveData<List<TrainingPlan>> = _recommendedPlans

    private val _goalProgress = MutableLiveData<Double?>()
    val goalProgress: LiveData<Double?> = _goalProgress

    class Factory(
        private val trainingPlanRepository: TrainingPlanRepository,
        private val notificationRepository: NotificationRepository,
        private val runSessionRepository: RunSessionRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TrainingPlanViewModel(
                    trainingPlanRepository,
                    notificationRepository,
                    runSessionRepository
                ) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel")
            }
        }
    }
}