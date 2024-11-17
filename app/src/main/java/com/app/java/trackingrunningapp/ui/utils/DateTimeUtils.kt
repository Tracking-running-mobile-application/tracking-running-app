package com.app.java.trackingrunningapp.ui.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Clock.*
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object DateTimeUtils {
    fun getCurrentDate(): kotlinx.datetime.LocalDate {
        val currentInstant = System.now()
        val userTimeZone = TimeZone.currentSystemDefault()
        return currentInstant.toLocalDateTime(userTimeZone).date
    }

    fun getCurrentDateTime(): kotlinx.datetime.LocalDateTime {
        return System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }
}