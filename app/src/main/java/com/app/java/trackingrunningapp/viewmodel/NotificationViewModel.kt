package com.app.java.trackingrunningapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.ui.data.repositories.NotificationRepository
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val notificationRepository: NotificationRepository,
): ViewModel() {

    fun triggerNotification(
        sessionId: Int?,
        title: String,
        message: String,
        notificationType: String
    ) {
        viewModelScope.launch {
            notificationRepository.createNotification(
                sessionId = sessionId,
                title = title,
                message = message,
                notificationType = notificationType,
            )
        }
    }

    fun deleteNotification(sessionId: Int) {
        viewModelScope.launch {
            notificationRepository.deleteNotification(sessionId)
        }
    }

    fun getAllNotification() {
        viewModelScope.launch {
            notificationRepository.getAllNotification()
        }
    }

}