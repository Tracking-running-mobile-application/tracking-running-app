
/***TODO: Remove! Find how to trigger noti
package com.app.java.trackingrunningapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.model.repositories.NotificationRepository
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

    fun getAllNotification() {
        viewModelScope.launch {
            notificationRepository.getAllNotification()
        }
    }

}
 ***/