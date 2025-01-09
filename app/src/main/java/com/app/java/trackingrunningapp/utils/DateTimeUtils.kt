package com.app.java.trackingrunningapp.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Clock.System
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter

object DateTimeUtils {
    private const val DATE_FORMAT = "dd/MM/yyyy"

    fun formatDateString(value: String): String {
        val inputDate = DateTimeFormatter.ofPattern("yyyyMMdd")
        val date = java.time.LocalDate.parse(value,inputDate)
        val outputDate = DateTimeFormatter.ofPattern(DATE_FORMAT)
        return date.format(outputDate)
    }

    fun formatDateStringRemoveHyphen(date: String): String {
        return date.replace("-", "")
    }


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
        val currentMonth = getCurrentDate().month.value
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

    fun extractMonthYearFromDate(dateString: String): String {
        val date = LocalDate.parse(dateString)
        val month = date.month.toString().padStart(2, '0')
        val year = date.year.toString()
        return "$month$year"
    }

    fun getEveryMonthOfYear(): List<String> {
        val currentYear = getCurrentDate().year
        return (1..12).map { month ->
            "%02d%04d".format(month, currentYear)
        }
    }
}