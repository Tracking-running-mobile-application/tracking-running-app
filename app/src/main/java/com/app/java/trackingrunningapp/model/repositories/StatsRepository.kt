package com.app.java.trackingrunningapp.model.repositories

import com.app.java.trackingrunningapp.model.DAOs.MonthlyStatsDao
import com.app.java.trackingrunningapp.model.DAOs.WeeklyStatsDao
import com.app.java.trackingrunningapp.model.DAOs.YearlyStatsDao
import com.app.java.trackingrunningapp.model.database.InitDatabase
import com.app.java.trackingrunningapp.model.entities.MonthlyStats
import com.app.java.trackingrunningapp.model.entities.WeeklyStats
import com.app.java.trackingrunningapp.model.entities.YearlyStats
import com.app.java.trackingrunningapp.ui.utils.DateTimeUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus

class StatsRepository(
    private val coroutineScope: CoroutineScope

) {
    val db = InitDatabase.runningDatabase

    private val yearlyStatsDao: YearlyStatsDao = db.yearlyStatsDao()
    private val monthlyStatsDao: MonthlyStatsDao = db.monthlyStatsDao()
    private val weeklyStatsDao: WeeklyStatsDao = db.weeklyStatsDao()

    private val weeklyKeys = mutableSetOf<String>()
    private val monthlyKeys = mutableSetOf<String>()
    private val yearlyKeys = mutableSetOf<String>()

    private val _weeklyStatsMap = MutableStateFlow<Map<String, WeeklyStats>>(emptyMap())
    val weeklyStatsMap: StateFlow<Map<String, WeeklyStats>> = _weeklyStatsMap

    private val _monthlyStatsMap = MutableStateFlow<Map<String, MonthlyStats>>(emptyMap())
    val monthlyStatsMap: StateFlow<Map<String, MonthlyStats>> = _monthlyStatsMap

    private val _yearlyStatsMap = MutableStateFlow<Map<String, YearlyStats>>(emptyMap())
    val yearlyStatsMap: StateFlow<Map<String, YearlyStats>> = _yearlyStatsMap

    suspend fun addStatsMultipleWeeks(weeklyStats: List<WeeklyStats>) {
        weeklyStats.forEach { stats ->
            weeklyStatsDao.upsertWeeklyStats(stats)
            _weeklyStatsMap.value = _weeklyStatsMap.value.toMutableMap().apply {
                this[stats.weeklyStatsKey] = stats
            }
        }
    }

    suspend fun addStatsMultipleMonths(monthlyStats: List<MonthlyStats>) {
        monthlyStats.forEach { stats ->
            monthlyStatsDao.upsertMonthlyStats(stats)
            _monthlyStatsMap.value = _monthlyStatsMap.value.toMutableMap().apply {
                this[stats.monthStatsKey] = stats
            }
        }
    }

    suspend fun addStatsMultipleYears(yearlyStats: List<YearlyStats>) {
        yearlyStats.forEach { stats ->
            yearlyStatsDao.upsertYearlyStats(stats)
            _yearlyStatsMap.value = _yearlyStatsMap.value.toMutableMap().apply {
                this[stats.yearlyStatsKey] = stats
            }
        }
    }

    private fun updateWeekKey(currentWeekKey: MutableSet<String>): Set<String> {
        val today = DateTimeUtils.getCurrentDate().toString()
        if (!currentWeekKey.contains(today)) {
            currentWeekKey.add(today)
        }
        return currentWeekKey
    }

    private fun updateMonthKey(currentMonthKey: MutableSet<String>): Set<String> {
        val firstDayOfWeek = DateTimeUtils.getFirstDayOfCurrentWeek().toString()
        if (!currentMonthKey.contains(firstDayOfWeek)) {
            currentMonthKey.add(firstDayOfWeek)
        }
        return currentMonthKey
    }

    private fun updateYearKey(currentYearKey: MutableSet<String>): Set<String> {
        val year = DateTimeUtils.getCurrentDate().year.toString()
        if (!currentYearKey.contains(year)) {
            currentYearKey.add(year)
        }
        return currentYearKey
    }


    private fun updateKeys() {
        updateWeekKey(weeklyKeys)
        updateMonthKey(monthlyKeys)
        updateYearKey(yearlyKeys)
    }

    fun startUpdatingKeys() {
        coroutineScope.launch {
            while (isActive) {
                val now = DateTimeUtils.getCurrentDateTime()
                val nextMidnight = now.date.plus(1, DateTimeUnit.DAY)
                    .atStartOfDayIn(TimeZone.currentSystemDefault())

                val nowInstant = DateTimeUtils.getCurrentInstant()
                val delayUntilMidnight = nextMidnight.toEpochMilliseconds() - nowInstant.toEpochMilliseconds()

                delay(delayUntilMidnight)
                updateKeys()
            }
        }
    }

}