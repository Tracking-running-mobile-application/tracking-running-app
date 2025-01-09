package com.app.java.trackingrunningapp.data.repository

import com.app.java.trackingrunningapp.data.dao.MonthlyStatsDao
import com.app.java.trackingrunningapp.data.dao.WeeklyStatsDao
import com.app.java.trackingrunningapp.data.dao.YearlyStatsDao
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.model.entity.MonthlyStats
import com.app.java.trackingrunningapp.data.model.entity.WeeklyStats
import com.app.java.trackingrunningapp.data.model.entity.YearlyStats
import com.app.java.trackingrunningapp.utils.DateTimeUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
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

        updatedWeeklyMap[stats.weeklyStatsKey] = updatedWeeklyMap[stats.weeklyStatsKey]?.let { existingStats ->
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

        updatedMonthlyMap[stats.monthStatsKey] = updatedMonthlyMap[stats.monthStatsKey]?.let { existingStats ->
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

        updatedYearlyMap[stats.yearlyStatsKey] = updatedYearlyMap[stats.yearlyStatsKey]?.let { existingStats ->
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
        val date = DateTimeUtils.getCurrentDate().toString()
        val todayInString = DateTimeUtils.formatDateStringRemoveHyphen(date)
        val sessions = runSessionRepository.filterRunningSessionByDay(todayInString, todayInString)

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
                weeklyStatsKey = date,
                totalDistance = totalDistance,
                totalDuration = totalDuration,
                totalCaloriesBurned = totalCaloriesBurned,
                totalAvgPace = totalAvgPace
            )
        )
    }

    suspend fun calculateMonthlyStats() {
        val weeklyMap = _weeklyStatsMap.value
        val firstDaysOfWeek = DateTimeUtils.getEveryFirstDayOfWeekInCurrentMonth()

        firstDaysOfWeek.forEach { firstDayOfWeek ->
            val startOfWeek = LocalDate.parse(firstDayOfWeek)
            val endOfWeek = startOfWeek.plus(6, DateTimeUnit.DAY)

            val weeklySessions = weeklyMap.filter { (dayKey, _) ->
                val dayDate = LocalDate.parse(dayKey)
                dayDate in startOfWeek..endOfWeek
            }

            val totalDistance = weeklySessions.values.sumOf { it.totalDistance ?: 0.0 }
            val totalDuration = weeklySessions.values.sumOf { it.totalDuration ?: 0L }
            val totalCaloriesBurned = weeklySessions.values.sumOf { it.totalCaloriesBurned ?: 0.0 }
            val totalAvgPace = if (totalDistance > 0) {
                (totalDuration / 60.0) / totalDistance
            } else {
                0.0
            }

            addStatsMonthly(
                MonthlyStats(
                    monthStatsKey = firstDayOfWeek,
                    totalDistance = totalDistance,
                    totalDuration = totalDuration,
                    totalCaloriesBurned = totalCaloriesBurned,
                    totalAvgPace = totalAvgPace
                )
            )
        }
    }

    suspend fun calculateYearlyStats() {
        val monthlyMap = _monthlyStatsMap.value.toMutableMap()
        val firstDaysOfWeeks = DateTimeUtils.getEveryFirstDayOfWeekInCurrentMonth()
        val monthsOfYear = DateTimeUtils.getEveryMonthOfYear()

        firstDaysOfWeeks.forEach { firstDayOfWeek ->
            val monthlyStats = monthlyMap[firstDayOfWeek]

            if (monthlyStats != null) {
                monthlyMap[firstDayOfWeek] = monthlyMap[firstDayOfWeek]?.apply {
                    totalDistance = totalDistance?.plus(monthlyStats.totalDistance ?: 0.0)
                    totalDuration = totalDuration?.plus(monthlyStats.totalDuration ?: 0L)
                    totalCaloriesBurned =
                        totalCaloriesBurned?.plus(monthlyStats.totalCaloriesBurned ?: 0.0)
                    totalAvgPace = totalAvgPace?.plus(monthlyStats.totalAvgPace ?: 0.0)
                } ?: monthlyStats
            }
        }

        monthsOfYear.forEach { monthYearKey ->
            val monthlySessions = monthlyMap.filter { (weekKey, _) ->
                DateTimeUtils.extractMonthYearFromDate(weekKey) == monthYearKey
            }

            val totalDistance = monthlySessions.values.sumOf { it.totalDistance ?: 0.0 }
            val totalDuration = monthlySessions.values.sumOf { it.totalDuration ?: 0L }
            val totalCaloriesBurned = monthlySessions.values.sumOf { it.totalCaloriesBurned ?: 0.0 }
            val totalAvgPace = if (monthlySessions.isNotEmpty()) {
                monthlySessions.values.sumOf { it.totalAvgPace ?: 0.0 } / monthlySessions.size
            } else {
                0.0
            }

            addStatsYearly (
                YearlyStats (
                    yearlyStatsKey = monthYearKey,
                    totalDistance = totalDistance,
                    totalDuration = totalDuration,
                    totalCaloriesBurned = totalCaloriesBurned,
                    totalAvgPace = totalAvgPace
                )
            )
        }
    }
}