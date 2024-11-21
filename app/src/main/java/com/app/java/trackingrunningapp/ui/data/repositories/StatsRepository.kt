package com.app.java.trackingrunningapp.ui.data.repositories

import com.app.java.trackingrunningapp.ui.data.DAOs.MonthlyStatsDao
import com.app.java.trackingrunningapp.ui.data.DAOs.WeeklyStatsDao
import com.app.java.trackingrunningapp.ui.data.DAOs.YearlyStatsDao
import com.app.java.trackingrunningapp.ui.data.entities.MonthlyStats
import com.app.java.trackingrunningapp.ui.data.entities.WeeklyStats
import com.app.java.trackingrunningapp.ui.data.entities.YearlyStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StatsRepository(
    private val yearlyStatsDao: YearlyStatsDao,
    private val monthlyStatsDao: MonthlyStatsDao,
    private val weeklyStatsDao: WeeklyStatsDao
) {
    private val _weeklyStatsMap = MutableStateFlow<Map<String, WeeklyStats>>(emptyMap())
    val weeklyStatsMap : StateFlow<Map<String, WeeklyStats>> = _weeklyStatsMap

    private val _monthlyStatsMap = MutableStateFlow<Map<String, MonthlyStats>>(emptyMap())
    val monthlyStatsMap : StateFlow<Map<String, MonthlyStats>> = _monthlyStatsMap

    private val _yearlyStatsMap = MutableStateFlow<Map<String, YearlyStats>>(emptyMap())
    val yearlyStatsMap : StateFlow<Map<String, YearlyStats>> = _yearlyStatsMap

    suspend fun addStatsMultipleWeeks(weeklyStats: List<WeeklyStats>) {

    }

    suspend fun addStatsForMonth(
        monthKey: String,
        distance: Double,
        duration: Long,
        caloriesBurned: Double,
        pace: Double,
        sessionSize: Int
    ) {
        val monthStats = _monthlyStatsMap.value[monthKey]?.copy()
            ?: monthlyStatsDao.getMonthlyStats(monthKey)
            ?: MonthlyStats(monthKey)

        var totalPace: Double = 0.0

        monthStats.totalDistance = monthStats.totalDistance?.plus(distance)
        monthStats.totalDuration = monthStats.totalDuration?.plus(duration)
        monthStats.totalCaloriesBurned = monthStats.totalCaloriesBurned?.plus(caloriesBurned)
        totalPace += pace
        monthStats.totalAvgPace = totalPace / sessionSize



        monthlyStatsDao.upsertMonthlyStats(monthStats)
        _monthlyStatsMap.value = _monthlyStatsMap.value.toMutableMap().apply {
            this[monthKey] = monthStats
        }
    }

    suspend fun addStatsForYear(
        yearKey: String,
        distance: Double,
        duration: Long,
        caloriesBurned: Double,
        pace: Double,
        sessionSize: Int
    ) {
        val yearStats = _yearlyStatsMap.value[yearKey]?.copy()
            ?: yearlyStatsDao.getYearlyStats(yearKey)
            ?: YearlyStats(yearKey)

        var totalPace : Double = 0.0

        yearStats.totalDistance = yearStats.totalDistance?.plus(distance)
        yearStats.totalDuration = yearStats.totalDuration?.plus(duration)
        yearStats.totalCaloriesBurned = yearStats.totalCaloriesBurned?.plus(caloriesBurned)
        totalPace += pace
        yearStats.totalAvgPace = totalPace / sessionSize

        yearlyStatsDao.upsertYearlyStats(yearStats)
        _yearlyStatsMap.value = _yearlyStatsMap.value.toMutableMap().apply {
            this[yearKey] = yearStats
        }
    }

}
