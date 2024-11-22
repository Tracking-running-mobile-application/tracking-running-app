package com.app.java.trackingrunningapp.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.ui.data.entities.MonthlyStats
import com.app.java.trackingrunningapp.ui.data.entities.WeeklyStats
import com.app.java.trackingrunningapp.ui.data.entities.YearlyStats
import com.app.java.trackingrunningapp.ui.data.models.Quadruple
import com.app.java.trackingrunningapp.ui.data.repositories.RunSessionRepository
import com.app.java.trackingrunningapp.ui.data.repositories.StatsRepository
import com.app.java.trackingrunningapp.ui.utils.DateTimeUtils
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class StatsViewModel(
    private val statsRepository: StatsRepository,
    private val runSessionRepository: RunSessionRepository
): ViewModel() {
    init {
        statsRepository.startUpdatingKeys()
        calcStatsForWeek(statsRepository.weeklyKeys)
        calcStatsForMonth(statsRepository.monthlyKeys)
        calcStatsForYear(statsRepository.yearlyKeys, statsRepository.monthlyKeys)
    }

    fun getWeeklyStatsAsList(): LiveData<List<WeeklyStats>> {
        return statsRepository.weeklyStatsMap.map { it.values.toList() }
            .asLiveData()
    }

    fun getMonthlyStatsAsList(): LiveData<List<MonthlyStats>> {
        return statsRepository.monthlyStatsMap.map { it.values.toList() }
            .asLiveData()
    }

    fun getYearlyStatsAsList(): LiveData<List<YearlyStats>> {
        return statsRepository.yearlyStatsMap.map { it.values.toList() }
            .asLiveData()
    }

    private fun calcStatsForWeek(currentDays: Set<String>) {
        viewModelScope.launch {
            val weeklyStats = currentDays.map { today ->
                val sessions = runSessionRepository.filterRunningSessionByDay(today, today)
                val sessionSize = sessions.size + 1

                val aggregatedStats = sessions.fold(Quadruple(0.0, 0L, 0.0, 0.0)) { acc, session ->
                    Quadruple(
                        acc.first + session.distance,
                        acc.second + session.duration,
                        acc.third + session.caloriesBurned,
                        acc.fourth + session.pace
                    )
                }

                WeeklyStats(
                    weeklyStatsKey = today,
                    totalDistance = aggregatedStats.first,
                    totalDuration = aggregatedStats.second,
                    totalCaloriesBurned = aggregatedStats.third,
                    totalAvgPace = aggregatedStats.fourth / sessionSize
                )
            }

            statsRepository.addStatsMultipleWeeks(weeklyStats)
        }
    }

    private fun calcStatsForMonth(firstDayOfWeeks: Set<String>) {
        viewModelScope.launch {
            val monthlyStats = firstDayOfWeeks.map { firstDayOfWeek ->
                val lastDayOfWeek = DateTimeUtils.getLastDayOfCurrentWeek().toString()

                val sessions = runSessionRepository.filterRunningSessionByDay(firstDayOfWeek, lastDayOfWeek)
                val sessionSize = sessions.size + 1

                val aggregatedStats = sessions.fold(Quadruple(0.0, 0L, 0.0, 0.0)) {acc, session ->
                    Quadruple (
                    acc.first + session.distance,
                    acc.second + session.duration,
                    acc.third + session.caloriesBurned,
                    acc.fourth + session.pace
                    )
                }

                    MonthlyStats(
                        monthStatsKey = firstDayOfWeek,
                        totalDistance = aggregatedStats.first,
                        totalDuration = aggregatedStats.second,
                        totalCaloriesBurned = aggregatedStats.third,
                        totalAvgPace = aggregatedStats.fourth / sessionSize,
                    )
                }
            statsRepository.addStatsMultipleMonths(monthlyStats)
        }
    }

    private fun calcStatsForYear(years: Set<String>, firstDayOfWeeks: Set<String>) {
        viewModelScope.launch {
            statsRepository.monthlyStatsMap.collect { monthlyStats ->
                val sessionSize = firstDayOfWeeks.size + 1

                val yearlyStats = years.map { year ->
                    var totalDistance = 0.0
                    var totalDuration = 0L
                    var totalCaloriesBurned = 0.0
                    var totalPace = 0.0

                    for (day in firstDayOfWeeks) {
                        val statsForWeek = monthlyStats[day]
                        if (statsForWeek != null) {
                            totalDistance += statsForWeek.totalDistance ?: 0.0
                            totalDuration += statsForWeek.totalDuration ?: 0L
                            totalCaloriesBurned += statsForWeek.totalCaloriesBurned ?: 0.0
                            totalPace += statsForWeek.totalAvgPace ?: 0.0
                        }
                    }

                    YearlyStats(
                        yearlyStatsKey = year,
                        totalDistance = totalDistance,
                        totalDuration = totalDuration,
                        totalCaloriesBurned = totalCaloriesBurned,
                        totalAvgPace = totalPace / sessionSize
                    )
                }

                statsRepository.addStatsMultipleYears(yearlyStats)
            }
        }
    }
}