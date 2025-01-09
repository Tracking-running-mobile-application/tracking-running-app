package com.app.java.trackingrunningapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.data.model.entity.MonthlyStats
import com.app.java.trackingrunningapp.data.model.entity.WeeklyStats
import com.app.java.trackingrunningapp.data.model.entity.YearlyStats
import com.app.java.trackingrunningapp.data.repository.StatsRepository
import com.app.java.trackingrunningapp.utils.DateTimeUtils
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import java.util.Locale

class StatsViewModel(
    private val statsRepository: StatsRepository
): ViewModel() {
    val weeklyStats: LiveData<Map<String, WeeklyStats>> = statsRepository.weeklyStatsMap.asLiveData()
    val monthlyStats: LiveData<Map<String, MonthlyStats>> = statsRepository.monthlyStatsMap.asLiveData()
    val yearlyStats: LiveData<Map<String, YearlyStats>> = statsRepository.yearlyStatsMap.asLiveData()

    val currentWeekStats: LiveData<List<WeeklyStats>> = weeklyStats.map { map ->
        val currentWeek = DateTimeUtils.getFirstDayOfCurrentWeek()

        (0..6).map { dayOffset ->
            val currentDay = currentWeek.plus(dayOffset, DateTimeUnit.DAY).toString()
            map[currentDay] ?: WeeklyStats(
                weeklyStatsKey = currentDay,
                totalDistance = 0.0,
                totalDuration = 0L,
                totalCaloriesBurned = 0.0,
                totalAvgPace = 0.0
            )
        }
    }

    val currentMonthStats: LiveData<List<MonthlyStats>> = monthlyStats.map { map ->
        val firstDayOfWeek = DateTimeUtils.getEveryFirstDayOfWeekInCurrentMonth()
        firstDayOfWeek.map { monthKey ->
            map[monthKey] ?: MonthlyStats(
                monthStatsKey = monthKey,
                totalDistance = 0.0,
                totalDuration = 0L,
                totalCaloriesBurned = 0.0,
                totalAvgPace = 0.0
            )
        }
    }

    val currentYearStats: LiveData<List<YearlyStats>> = yearlyStats.map { map ->
        val currentYear = DateTimeUtils.getCurrentDate().year.toString()
        (1..12).map { month ->
            val monthYearKey = String.format(Locale.US, "%02d-%s", month, currentYear)
            map[monthYearKey] ?: YearlyStats(
                yearlyStatsKey = monthYearKey,
                totalDistance = 0.0,
                totalDuration = 0L,
                totalCaloriesBurned = 0.0,
                totalAvgPace = 0.0
            )
        }
    }

    private fun refreshWeeklyStats() = viewModelScope.launch {
        statsRepository.calculateWeeklyStats()
    }

    private fun refreshMonthlyStats() = viewModelScope.launch {
        statsRepository.calculateMonthlyStats()
    }

    private fun refreshYearlyStats() = viewModelScope.launch {
        statsRepository.calculateYearlyStats()
    }

    fun refreshStats() {
        refreshWeeklyStats()
        refreshMonthlyStats()
        refreshYearlyStats()
    }
}