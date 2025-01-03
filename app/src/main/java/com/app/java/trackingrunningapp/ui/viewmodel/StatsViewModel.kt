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

class StatsViewModel(
    private val statsRepository: StatsRepository
): ViewModel() {
    val weeklyStats: LiveData<Map<String, WeeklyStats>> = statsRepository.weeklyStatsMap.asLiveData()
    val monthlyStats: LiveData<Map<String, MonthlyStats>> = statsRepository.monthlyStatsMap.asLiveData()
    val yearlyStats: LiveData<Map<String, YearlyStats>> = statsRepository.yearlyStatsMap.asLiveData()

    val currentWeekStats: LiveData<List<WeeklyStats>> = weeklyStats.map { map ->
        val currentWeek = DateTimeUtils.getFirstDayOfCurrentWeek()
        val lastDayOfWeek = currentWeek.plus(6, DateTimeUnit.DAY)
        map.filter { (key, _) ->
            val date = LocalDate.parse(key)
            date in currentWeek..lastDayOfWeek
        }.values.toList()
    }

    val currentMonthStats: LiveData<List<MonthlyStats>> = monthlyStats.map { map ->
        val firstDayOfWeek = DateTimeUtils.getFirstDayOfCurrentWeek().toString()
        map.filterKeys { it == firstDayOfWeek }.values.toList()
    }

    val currentYearStats: LiveData<List<YearlyStats>> = yearlyStats.map { map ->
        val today = DateTimeUtils.getCurrentDate().toString()
        val currentYear = DateTimeUtils.extractMonthYearFromDate(today)
        map.filterKeys { it == currentYear }.values.toList()
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