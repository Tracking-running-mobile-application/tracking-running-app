package com.app.java.trackingrunningapp.ui.data.repository

import com.app.java.trackingrunningapp.ui.data.DAOs.MonthlyStatsDao
import com.app.java.trackingrunningapp.ui.data.DAOs.WeeklyStatsDao
import com.app.java.trackingrunningapp.ui.data.DAOs.YearlyStatsDao
import com.app.java.trackingrunningapp.ui.data.entities.MonthlyStats
import com.app.java.trackingrunningapp.ui.data.entities.WeeklyStats
import com.app.java.trackingrunningapp.ui.data.entities.YearlyStats

class StatsRepository(
    private val yearlyStatsDao: YearlyStatsDao,
    private val monthlyStatsDao: MonthlyStatsDao,
    private val weeklyStatsDao: WeeklyStatsDao
) {
    private val weeklyStatsMap: MutableMap<Int, WeeklyStats> = mutableMapOf()
    private val monthlyStatsMap: MutableMap<String, MonthlyStats> = mutableMapOf()
    private val yearlyStatsMap: MutableMap<Int, YearlyStats> = mutableMapOf()

    suspend fun addStatsForWeek(weekStart: Int, distance: Float, duration: Float, caloriesBurned: Float) {
        val weekStats = weeklyStatsMap.getOrPut(weekStart) {
            weeklyStatsDao.getWeeklyStats(weekStart) ?: WeeklyStats(weekStart)
        }

        weekStats.totalDistance += distance
        weekStats.totalDuration += duration
        weekStats.totalCaloriesBurned += caloriesBurned
        weekStats.totalPace += weekStats.totalDuration / weekStats.totalDistance

        weeklyStatsDao.upsertWeeklyStats(weekStats)
        weeklyStatsMap[weekStart] = weekStats
    }

    suspend fun addStatsForMonth(monthKey: String, distance: Float, duration: Float, caloriesBurned: Float) {
        val monthStats = monthlyStatsMap.getOrPut(monthKey) {
            monthlyStatsDao.getMonthlyStats(monthKey) ?: MonthlyStats(monthKey)
        }

        monthStats.totalDistance += distance
        monthStats.totalDuration += duration
        monthStats.totalCaloriesBurned += caloriesBurned
        monthStats.totalPace += monthStats.totalDuration / monthStats.totalDistance

        monthlyStatsDao.upsertMonthlyStats(monthStats)
        monthlyStatsMap[monthKey] = monthStats
    }

    suspend fun addStatsForYear(year: Int, distance: Float, duration: Float, caloriesBurned: Float ) {
        val yearStats = yearlyStatsMap.getOrPut(year) {
            yearlyStatsDao.getYearlyStats(year) ?: YearlyStats(year)
        }

        yearStats.totalDistance += distance
        yearStats.totalDuration += duration
        yearStats.totalCaloriesBurned += caloriesBurned
        yearStats.totalPace = yearStats.totalDuration / yearStats.totalDistance

        yearlyStatsDao.upsertYearlyStats(yearStats)
        yearlyStatsMap[year] = yearStats
    }
}
