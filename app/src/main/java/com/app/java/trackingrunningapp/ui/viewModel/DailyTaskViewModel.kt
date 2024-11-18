package com.app.java.trackingrunningapp.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.java.trackingrunningapp.ui.data.repository.DailyTaskRepository
import com.app.java.trackingrunningapp.ui.data.repository.NotificationRepository

class DailyTaskViewModel(
    private val dailyTaskRepository: DailyTaskRepository,
    private val notificationRepository: NotificationRepository
): ViewModel() {

    val progressDistance = MutableLiveData<Float>()

    fun updateProgress(planId: Int, progress: Float) {

    }
}