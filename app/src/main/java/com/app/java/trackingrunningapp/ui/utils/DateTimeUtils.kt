package com.app.java.trackingrunningapp.ui.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Clock.*
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
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

    fun fromLocalDateTime(dateTime: LocalDateTime): String {
        return dateTime.toString()
    }

    fun getCurrentInstant(): Instant {
        return Clock.System.now()
    }

    fun instantToString(dateInstant: Instant): String {
        return dateInstant.toString()
    }

    fun durationToSeconds(formattedDuration: String): Long {
        val parts = formattedDuration.split(":").map { it.toInt() }
        return when (parts.size) {
            3 -> parts[0] * 3600L + parts[1] * 60L + parts[2]
            2 -> parts[0] * 60L + parts[1]
            else -> 0L
        }
    }

    fun calculateDuration(startTime: Instant, endTime: Instant = Clock.System.now()): String {
        val durationInSeconds = endTime.epochSeconds - startTime.epochSeconds

        return formatDuration(durationInSeconds)
    }

    /*placed in here for the sake of simplicity for RunSessionViewModel*/
    fun formatDuration(seconds: Long): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60

        return if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, secs)
        } else {
            String.format("%02d:%02d", minutes, secs)
        }
    }

    fun getCurrentTime(): LocalTime {
        val currentInstant = Clock.System.now()
        val userTimeZone = TimeZone.currentSystemDefault()
        return currentInstant.toLocalDateTime(userTimeZone).time
    }
}