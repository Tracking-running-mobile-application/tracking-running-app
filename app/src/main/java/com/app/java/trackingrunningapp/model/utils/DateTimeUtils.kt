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


    fun getCurrentTime(): LocalTime {
        val currentInstant = Clock.System.now()
        val userTimeZone = TimeZone.currentSystemDefault()
        return currentInstant.toLocalDateTime(userTimeZone).time
    }
}