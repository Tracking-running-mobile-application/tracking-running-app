package com.app.java.trackingrunningapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.model.entities.MonthlyStats
import com.app.java.trackingrunningapp.model.entities.WeeklyStats
import com.app.java.trackingrunningapp.model.entities.YearlyStats
import com.app.java.trackingrunningapp.model.models.Quadruple
import com.app.java.trackingrunningapp.model.repositories.RunSessionRepository
import com.app.java.trackingrunningapp.model.repositories.StatsRepository
import com.app.java.trackingrunningapp.ui.utils.DateTimeUtils
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class StatsViewModel(
    private val statsRepository: StatsRepository,
    private val runSessionRepository: RunSessionRepository
): ViewModel() {
    private val mockTestWeeklyStat: Set<String> =
        setOf(
            "2024-11-22", "2024-11-23", "2024-11-24", "2024-11-25", "2024-11-26", "2024-11-27",
            "2024-11-28", "2024-11-29", "2024-11-30", "2024-12-01", "2024-12-02", "2024-12-03",
            "2024-12-04", "2024-12-05", "2024-12-06", "2024-12-07", "2024-12-08", "2024-12-09",
            "2024-12-10", "2024-12-11", "2024-12-12", "2024-12-13", "2024-12-14", "2024-12-15",
            "2024-12-16", "2024-12-17", "2024-12-18", "2024-12-19", "2024-12-20", "2024-12-21",
            "2024-12-22", "2024-12-23", "2024-12-24", "2024-12-25", "2024-12-26", "2024-12-27",
            "2024-12-28", "2024-12-29", "2024-12-30", "2024-12-31"
        )

    private val mockTestMonthlyStat: Set<String> =
        setOf(
            "2024-11-25", "2024-12-02", "2024-12-09", "2024-12-16", "2024-12-23", "2024-12-30"
        )

    private val mockTestYearlyStat: Set<String> =
        setOf("2024")

    init {
        statsRepository.startUpdatingKeys()
        /*calcStatsForWeek(statsRepository.weeklyKeys)
        calcStatsForMonth(statsRepository.monthlyKeys)
        calcStatsForYear(statsRepository.yearlyKeys, statsRepository.monthlyKeys) */

        calcStatsForWeek(mockTestWeeklyStat)
        calcStatsForMonth(mockTestMonthlyStat)
        calcStatsForYear(mockTestYearlyStat, mockTestMonthlyStat)

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