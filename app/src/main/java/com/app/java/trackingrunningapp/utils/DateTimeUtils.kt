package com.app.java.trackingrunningapp.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Clock.System
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateTimeUtils {
    private const val DATE_FORMAT = "dd/MM/yyyy"
    fun formatDateString(value: String): String {
        val formats = listOf(
            DateTimeFormatter.ofPattern("yyyyMMdd"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        )

        val date = formats.asSequence()
            .mapNotNull { formatter ->
                try {
                    java.time.LocalDate.parse(value, formatter)
                } catch (e: Exception) {
                    null
                }
            }
            .firstOrNull() ?: throw IllegalArgumentException("Invalid date format: $value")

        val outputDate = DateTimeFormatter.ofPattern(DATE_FORMAT)
        return date.format(outputDate)
    }

    fun formatDate(inputDate: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val outputFormatter = DateTimeFormatter.ofPattern("dd/MM")
        val date = java.time.LocalDate.parse(inputDate, inputFormatter)
        return date.format(outputFormatter)
    }

    fun formatDateHistoryDetailFormat(inputDate: String): String {
        val inputFormatter = SimpleDateFormat("yyyyMMdd", Locale.ENGLISH)
        val date = inputFormatter.parse(inputDate)
        val outputFormatter = SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH)
        return outputFormatter.format(date!!)
    }

    fun getMonthNameFromYearMonth(input: String): String {
        val formatter = DateTimeFormatter.ofPattern("MM-yyyy", Locale.ENGLISH)
        val yearMonth = YearMonth.parse(input, formatter)
        return yearMonth.month.name
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

    private fun Int.isLeapYear(): Boolean {
        return (this % 4 == 0 && this % 100 != 0) || (this % 400 == 0)
    }

    fun getCurrentTime(): LocalTime {
        val currentInstant = Clock.System.now()
        val userTimeZone = TimeZone.currentSystemDefault()
        return currentInstant.toLocalDateTime(userTimeZone).time
    }

    fun extractMonthYearFromDate(dateString: String): String {
        val year = dateString.substring(0, 4)
        val month = dateString.substring(4, 6)

        return "$month-$year"
    }

    fun getFirstDaysOfMonth(): List<String> {
        val currentDate = getCurrentDate()
        return (1..12).map { month ->
            val firstDay = LocalDate(currentDate.year, month, 1)
            firstDay.toString().replace("-", "")
        }
    }

    fun getLastDaysOfMonth(): List<String> {
        val currentDate = getCurrentDate()
        return (1..12).map { month ->
            val lastDay = LocalDate(currentDate.year, month, 1).plus(1, DateTimeUnit.MONTH)
                .minus(1, DateTimeUnit.DAY)
            lastDay.toString().replace("-", "")
        }
    }

    fun getEveryMonthOfYear(): List<String> {
        val currentYear = getCurrentDate().year
        return (1..12).map { month ->
            "%02d%04d".format(month, currentYear)
        }
    }
}