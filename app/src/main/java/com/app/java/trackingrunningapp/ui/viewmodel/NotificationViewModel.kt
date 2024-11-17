package com.app.java.trackingrunningapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.ui.data.repository.NotificationRepository
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class NotificationViewModel(
    private val notificationRepository: NotificationRepository
): ViewModel() {

    fun getCurrentDateTime(): LocalDateTime {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }

    fun triggerNotification(
        sessionId: Int?,
        title: String,
        message: String,
        notificationType: String
    ) {
        viewModelScope.launch {
            val timeTriggered = getCurrentDateTime()
            notificationRepository.createNotification(
                sessionId = sessionId,
                title = title,
                message = message,
                notificationType = notificationType,
                timeTriggered = timeTriggered
            )
        }
    }

    fun deleteNotification(sessionId: Int) {
        viewModelScope.launch {
            notificationRepository.deleteNotification(sessionId)
        }
    }
}