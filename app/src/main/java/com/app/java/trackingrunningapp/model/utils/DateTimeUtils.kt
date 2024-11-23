package com.app.java.trackingrunningapp.ui.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Clock.*
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

object DateTimeUtils {
    fun getCurrentDate(): LocalDate {
        val currentInstant = System.now()
        val userTimeZone = TimeZone.currentSystemDefault()
        return currentInstant.toLocalDateTime(userTimeZone).date
    }

    fun getCurrentDateTime(): LocalDateTime {
        return System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }

    fun getCurrentInstant(): Instant {
        return Clock.System.now()
    }

    fun getFirstDayOfCurrentWeek(): LocalDate {
        val today = getCurrentDate()
        val daysFromMonday = today.dayOfWeek.ordinal
        return today.minus(daysFromMonday, DateTimeUnit.DAY)
    }

    fun getLastDayOfCurrentWeek(): LocalDate {
        val today = getCurrentDate()
        val daysToSunday = 7 - today.dayOfWeek.ordinal - 1
        return today.plus(daysToSunday, DateTimeUnit.DAY)
    }

    fun getEveryFirstDayOfWeekInCurrentMonth(): List<String> {
        val firstDayOfWeek = mutableListOf<String>()
        val currentMonth = DateTimeUtils.getCurrentDate().month.value
        var firstDayOfMonth = getFirstDayOfCurrentMonth()

        while (firstDayOfMonth.month.value == currentMonth) {
            firstDayOfWeek.add(firstDayOfMonth.toString())
            firstDayOfMonth = firstDayOfMonth.plus(7, DateTimeUnit.DAY)
        }

        return firstDayOfWeek
    }

    fun getFirstDayOfCurrentMonth(): LocalDate {
        val today = getCurrentDate()
        return LocalDate(today.year, today.month, 1)
    }

    fun getLastDayOfCurrentMonth(): LocalDate {
        val today = getCurrentDate()
        val lastDay = today.month.length(today.year.isLeapYear())
        return LocalDate(today.year, today.month, lastDay)
    }

    private fun Int.isLeapYear(): Boolean {
        return (this % 4 == 0 && this % 100 != 0) || (this % 400 == 0)
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
    private fun formatDuration(seconds: Long): String {
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