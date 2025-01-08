package com.app.java.trackingrunningapp.data.repository

import com.app.java.trackingrunningapp.data.dao.MonthlyStatsDao
import com.app.java.trackingrunningapp.data.dao.WeeklyStatsDao
import com.app.java.trackingrunningapp.data.dao.YearlyStatsDao
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.model.entity.MonthlyStats
import com.app.java.trackingrunningapp.data.model.entity.WeeklyStats
import com.app.java.trackingrunningapp.data.model.entity.YearlyStats
import com.app.java.trackingrunningapp.utils.DateTimeUtils
import com.app.java.trackingrunningapp.utils.StatsUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus

class StatsRepository {
    val db = InitDatabase.runningDatabase

    private val yearlyStatsDao: YearlyStatsDao = db.yearlyStatsDao()
    private val monthlyStatsDao: MonthlyStatsDao = db.monthlyStatsDao()
    private val weeklyStatsDao: WeeklyStatsDao = db.weeklyStatsDao()

    private val runSessionRepository: RunSessionRepository = InitDatabase.runSessionRepository

    private val _weeklyStatsMap = MutableStateFlow<Map<String, WeeklyStats>>(emptyMap())
    val weeklyStatsMap: StateFlow<Map<String, WeeklyStats>> = _weeklyStatsMap

    private val _monthlyStatsMap = MutableStateFlow<Map<String, MonthlyStats>>(emptyMap())
    val monthlyStatsMap: StateFlow<Map<String, MonthlyStats>> = _monthlyStatsMap

    private val _yearlyStatsMap = MutableStateFlow<Map<String, YearlyStats>>(emptyMap())
    val yearlyStatsMap: StateFlow<Map<String, YearlyStats>> = _yearlyStatsMap

    private suspend fun addStatsWeekly(stats: WeeklyStats) {
        val updatedWeeklyMap = _weeklyStatsMap.value.toMutableMap()
        val weeklyKey = DateTimeUtils.getCurrentDate().toString()

        updatedWeeklyMap[weeklyKey] = updatedWeeklyMap[weeklyKey]?.let { existingStats ->
            existingStats.apply {
                totalDistance = (totalDistance ?: 0.0) + (stats.totalDistance ?: 0.0)
                totalDuration = (totalDuration ?: 0L) + (stats.totalDuration ?: 0L)
                totalCaloriesBurned = (totalCaloriesBurned ?: 0.0) + (stats.totalCaloriesBurned ?: 0.0)
                totalAvgPace = if (totalDistance != 0.0) {
                    (totalDuration ?: 0L) / 60.0 / (totalDistance ?: 1.0)
                } else {
                    0.0
                }
            }
        } ?: stats


        if (updatedWeeklyMap.size > 365) {
            val oldestKeys = updatedWeeklyMap.keys.sorted().take(updatedWeeklyMap.size - 365)
            oldestKeys.forEach { updatedWeeklyMap.remove(it) }
        }

        _weeklyStatsMap.value = updatedWeeklyMap
        weeklyStatsDao.upsertWeeklyStats(stats)
    }

    private suspend fun addStatsMonthly(stats: MonthlyStats) {
        val updatedMonthlyMap = _monthlyStatsMap.value.toMutableMap()
        val monthlyKey = DateTimeUtils.getFirstDayOfCurrentWeek().toString()

        updatedMonthlyMap[monthlyKey] = updatedMonthlyMap[monthlyKey]?.let { existingStats ->
            existingStats.apply {
                totalDistance = (totalDistance ?: 0.0) + (stats.totalDistance ?: 0.0)
                totalDuration = (totalDuration ?: 0L) + (stats.totalDuration ?: 0L)
                totalCaloriesBurned = (totalCaloriesBurned ?: 0.0) + (stats.totalCaloriesBurned ?: 0.0)
                totalAvgPace = if (totalDistance != 0.0) {
                    (totalDuration ?: 0L) / 60.0 / (totalDistance ?: 1.0)
                } else {
                    0.0
                }
            }
        } ?: stats

        if (updatedMonthlyMap.size > 52) {
            val oldestKeys = updatedMonthlyMap.keys.sorted().take(updatedMonthlyMap.size - 12)
            oldestKeys.forEach { updatedMonthlyMap.remove(it) }
        }

        _monthlyStatsMap.value = updatedMonthlyMap
        monthlyStatsDao.upsertMonthlyStats(stats)
    }

    private suspend fun addStatsYearly(stats: YearlyStats) {
        val updatedYearlyMap = _yearlyStatsMap.value.toMutableMap()
        val today = DateTimeUtils.getCurrentDate().toString()
        val yearlyKey = DateTimeUtils.extractMonthYearFromDate(today)

        updatedYearlyMap[yearlyKey] = updatedYearlyMap[yearlyKey]?.let { existingStats ->
            existingStats.apply {
                totalDistance = (totalDistance ?: 0.0) + (stats.totalDistance ?: 0.0)
                totalDuration = (totalDuration ?: 0L) + (stats.totalDuration ?: 0L)
                totalCaloriesBurned = (totalCaloriesBurned ?: 0.0) + (stats.totalCaloriesBurned ?: 0.0)
                totalAvgPace = if (totalDistance != 0.0) {
                    (totalDuration ?: 0L) / 60.0 / (totalDistance ?: 1.0)
                } else {
                    0.0
                }
            }
        } ?: stats

        if (updatedYearlyMap.size > 12) {
            val oldestKeys = updatedYearlyMap.keys.sorted().take(updatedYearlyMap.size - 12)
            oldestKeys.forEach { updatedYearlyMap.remove(it) }
        }

        _yearlyStatsMap.value = updatedYearlyMap
        yearlyStatsDao.upsertYearlyStats(stats)
    }

    suspend fun calculateWeeklyStats() {
        val today = DateTimeUtils.getCurrentDate().toString()
        val sessions = runSessionRepository.filterRunningSessionByDay(today, today)

        val totalDistance = sessions.sumOf { it.distance ?: 0.0 }
        val totalDuration = sessions.sumOf { it.duration ?: 0L }
        val totalCaloriesBurned = sessions.sumOf { it.caloriesBurned ?: 0.0 }
        val totalAvgPace = if (totalDistance > 0) {
            (totalDuration / 60.0) / totalDistance
        } else {
            0.0
        }

        addStatsWeekly(
            WeeklyStats(
                weeklyStatsKey = today,
                totalDistance = totalDistance,
                totalDuration = totalDuration,
                totalCaloriesBurned = totalCaloriesBurned,
                totalAvgPace = totalAvgPace
            )
        )
    }

    suspend fun calculateMonthlyStats() {
        val weeklyMap = _weeklyStatsMap.value

        val firstDayOfWeek = DateTimeUtils.getFirstDayOfCurrentWeek()
        val lastDayOfWeek = firstDayOfWeek.plus(6, DateTimeUnit.DAY)

        val filteredSessions = weeklyMap.filter { (weekKey, _) ->
            val weekDate = LocalDate.parse(weekKey)
            weekDate in firstDayOfWeek..lastDayOfWeek
        }

        val totalDistance = filteredSessions.values.sumOf { it.totalDistance ?: 0.0 }
        val totalDuration = filteredSessions.values.sumOf { it.totalDuration ?: 0L }
        val totalCaloriesBurned = filteredSessions.values.sumOf { it.totalCaloriesBurned ?: 0.0 }
        val totalAvgPace = if (totalDistance > 0) {
            (totalDuration / 60.0) / totalDistance
        } else {
            0.0
        }

        addStatsMonthly(
            MonthlyStats(
                monthStatsKey = firstDayOfWeek.toString(),
                totalDistance = totalDistance,
                totalDuration = totalDuration,
                totalCaloriesBurned = totalCaloriesBurned,
                totalAvgPace = totalAvgPace
            )
        )
    }

    suspend fun calculateYearlyStats() {
        val monthlyMap = _monthlyStatsMap.value.toMutableMap()

        val today = DateTimeUtils.getCurrentDate().toString()
        val currentMonthYear = DateTimeUtils.extractMonthYearFromDate(today)

        var totalDistance = 0.0
        var totalDuration = 0L
        var totalCaloriesBurned = 0.0
        var totalAvgPace = 0.0
        var validWeekCount = 0

        monthlyMap.forEach { (monthlyKeys, monthlyStats) ->
            val monthYearInKey = DateTimeUtils.extractMonthYearFromDate(monthlyKeys)

            if (monthYearInKey == currentMonthYear) {
                totalDistance += monthlyStats.totalDistance ?: 0.0
                totalDuration += monthlyStats.totalDuration ?: 0L
                totalCaloriesBurned += monthlyStats.totalCaloriesBurned ?: 0.0
                totalAvgPace += monthlyStats.totalAvgPace ?: 0.0
                validWeekCount++
            }
        }
        val avgPace = if (validWeekCount > 0) totalAvgPace / validWeekCount else 0.0

        addStatsYearly(
            YearlyStats(
                yearlyStatsKey = currentMonthYear,
                totalDistance = totalDistance,
                totalDuration = totalDuration,
                totalCaloriesBurned = totalCaloriesBurned,
                totalAvgPace = avgPace
            )
        )
    }
}