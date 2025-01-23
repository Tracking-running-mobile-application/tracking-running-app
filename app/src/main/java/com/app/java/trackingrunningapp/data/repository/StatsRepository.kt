package com.app.java.trackingrunningapp.data.repository

import com.app.java.trackingrunningapp.data.dao.MonthlyStatsDao
import com.app.java.trackingrunningapp.data.dao.WeeklyStatsDao
import com.app.java.trackingrunningapp.data.dao.YearlyStatsDao
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.model.entity.MonthlyStats
import com.app.java.trackingrunningapp.data.model.entity.WeeklyStats
import com.app.java.trackingrunningapp.data.model.entity.YearlyStats
import com.app.java.trackingrunningapp.utils.DateTimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
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

        updatedWeeklyMap[stats.weeklyStatsKey] = stats

        if (updatedWeeklyMap.size > 365) {
            val oldestKeys = updatedWeeklyMap.keys.sorted().take(updatedWeeklyMap.size - 365)
            oldestKeys.forEach { updatedWeeklyMap.remove(it) }
        }

        _weeklyStatsMap.value = updatedWeeklyMap
        weeklyStatsDao.upsertWeeklyStats(stats)
    }

    private suspend fun addStatsMonthly(stats: MonthlyStats) {
        val updatedMonthlyMap = _monthlyStatsMap.value.toMutableMap()

        updatedMonthlyMap[stats.monthStatsKey] = stats

        if (updatedMonthlyMap.size > 52) {
            val oldestKeys = updatedMonthlyMap.keys.sorted().take(updatedMonthlyMap.size - 12)
            oldestKeys.forEach { updatedMonthlyMap.remove(it) }
        }

        _monthlyStatsMap.value = updatedMonthlyMap
        monthlyStatsDao.upsertMonthlyStats(stats)
    }

    private suspend fun addStatsYearly(stats: YearlyStats) {
        val updatedYearlyMap = _yearlyStatsMap.value.toMutableMap()

        updatedYearlyMap[stats.yearlyStatsKey] = stats

        if (updatedYearlyMap.size > 12) {
            val oldestKeys = updatedYearlyMap.keys.sorted().take(updatedYearlyMap.size - 12)
            oldestKeys.forEach { updatedYearlyMap.remove(it) }
        }

        _yearlyStatsMap.value = updatedYearlyMap
        yearlyStatsDao.upsertYearlyStats(stats)
    }

    suspend fun calculateWeeklyStats() {
        withContext(Dispatchers.IO) {
            val currentWeek = DateTimeUtils.getFirstDayOfCurrentWeek()

            (0..6).map { dayOffset ->
                val currentDay = currentWeek.plus(dayOffset, DateTimeUnit.DAY).toString()
                val currentDayWithoutHyphen = DateTimeUtils.formatDateStringRemoveHyphen(currentDay)
                val sessions = runSessionRepository.filterRunningSessionByDay(
                    currentDayWithoutHyphen,
                    currentDayWithoutHyphen
                )

                val totalDistance = sessions.sumOf { it.distance ?: 0.0 }
                val totalDuration = sessions.sumOf { it.duration ?: 0L }
                val totalCaloriesBurned = sessions.sumOf { it.caloriesBurned ?: 0.0 }
                val totalAvgSpeed = if (totalDistance > 0) {
                    totalDistance / (totalDuration / 3600)
                } else {
                    0.0
                }

                addStatsWeekly(
                    WeeklyStats(
                        weeklyStatsKey = currentDay,
                        totalDistance = totalDistance,
                        totalDuration = totalDuration,
                        totalCaloriesBurned = totalCaloriesBurned,
                        totalAvgSpeed = totalAvgSpeed
                    )
                )
            }
        }
    }

    suspend fun calculateMonthlyStats() {
        withContext(Dispatchers.IO) {
            val firstDaysOfWeek = DateTimeUtils.getEveryFirstDayOfWeekInCurrentMonth()

            firstDaysOfWeek.forEach { firstDayOfWeek ->
                val startOfWeek = LocalDate.parse(firstDayOfWeek)
                val endOfWeek = startOfWeek.plus(6, DateTimeUnit.DAY).toString()
                val sessions = runSessionRepository.filterRunningSessionByDay(
                    DateTimeUtils.formatDateStringRemoveHyphen(firstDayOfWeek),
                    DateTimeUtils.formatDateStringRemoveHyphen(endOfWeek)
                )

                val totalDistance = sessions.sumOf { it.distance ?: 0.0 }
                val totalDuration = sessions.sumOf { it.duration ?: 0L }
                val totalCaloriesBurned = sessions.sumOf { it.caloriesBurned ?: 0.0 }
                val totalAvgSpeed = if (totalDuration > 0) {
                    totalDistance / (totalDuration / 3600)
                } else {
                    0.0
                }

                addStatsMonthly(
                    MonthlyStats(
                        monthStatsKey = firstDayOfWeek,
                        totalDistance = totalDistance,
                        totalDuration = totalDuration,
                        totalCaloriesBurned = totalCaloriesBurned,
                        totalAvgSpeed = totalAvgSpeed
                    )
                )
            }
        }
    }

    suspend fun calculateYearlyStats() {
        withContext(Dispatchers.IO) {
            val firstDaysOfMonth = DateTimeUtils.getFirstDaysOfMonth()
            val lastDaysOfMonth = DateTimeUtils.getLastDaysOfMonth()

            firstDaysOfMonth.zip(lastDaysOfMonth).forEach { (firstDay, lastDay) ->
                val sessions = runSessionRepository.filterRunningSessionByDay(firstDay, lastDay)

                val totalDistance = sessions.sumOf { it.distance ?: 0.0 }
                val totalDuration = sessions.sumOf { it.duration ?: 0L }
                val totalCaloriesBurned = sessions.sumOf { it.caloriesBurned ?: 0.0 }
                val totalAvgSpeed = if (totalDistance > 0) {
                    totalDistance / (totalDuration / 3600)
                } else {
                    0.0
                }

                addStatsYearly(
                    YearlyStats(
                        yearlyStatsKey = DateTimeUtils.extractMonthYearFromDate(firstDay),
                        totalDistance = totalDistance,
                        totalDuration = totalDuration,
                        totalCaloriesBurned = totalCaloriesBurned,
                        totalAvgSpeed = totalAvgSpeed
                    )
                )
            }
        }
    }
}