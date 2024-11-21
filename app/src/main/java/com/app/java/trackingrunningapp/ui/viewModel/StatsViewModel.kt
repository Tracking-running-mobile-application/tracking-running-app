package com.app.java.trackingrunningapp.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.ui.data.entities.RunSession
import com.app.java.trackingrunningapp.ui.data.entities.WeeklyStats
import com.app.java.trackingrunningapp.ui.data.models.Quadruple
import com.app.java.trackingrunningapp.ui.data.repositories.RunSessionRepository
import com.app.java.trackingrunningapp.ui.data.repositories.StatsRepository
import com.app.java.trackingrunningapp.ui.utils.DateTimeUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StatsViewModel(
    private val statsRepository: StatsRepository,
    private val runSessionRepository: RunSessionRepository
): ViewModel() {
    private val _currentWeekSession = MutableStateFlow<List<RunSession>>(emptyList())
    val currentWeekSession: StateFlow<List<RunSession>> = _currentWeekSession

    private val _currentMonthSession = MutableStateFlow<List<RunSession>>(emptyList())
    val currentMonthSession: StateFlow<List<RunSession>> = _currentMonthSession

    private val _currentYearSession = MutableStateFlow<List<RunSession>>(emptyList())
    val currentYearSession: StateFlow<List<RunSession>> = _currentYearSession

    init {
        calcStatsForWeek()
        calcStatsForMonth()
        calcStatsForYear()
    }

    /***
     * TODO:
     * 1. Calc stats all at once in VM.
     * 2. Send to Repository, use a forEach to store them in a loop.
     * 3. Create maps in VM: MutableStateFlow and StateFlow
     * 4. Create funcs to generate keys in advance.
     * 5. Pass the values to map.
     * ***/
    fun calcStatsForWeek(weeks: List<String>) {
        viewModelScope.launch {
            val today = DateTimeUtils.getCurrentDate().toString()

            val weeklyStats = weeks.map { week ->
                val sessions = runSessionRepository.filterRunningSessionByDay(today, today)
                _currentWeekSession.value = sessions
                val sessionSize = sessions.size

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

    fun calcStatsForMonth() {
        viewModelScope.launch {
            val firstDayOfWeek = DateTimeUtils.getFirstDayOfCurrentWeek().toString()
            val lastDayOfWeek = DateTimeUtils.getLastDayOfCurrentWeek().toString()

            val sessions = runSessionRepository.filterRunningSessionByDay(firstDayOfWeek, lastDayOfWeek)
            _currentMonthSession.value = sessions
            val sessionSize = sessions.size

            sessions.forEach { session ->
                statsRepository.addStatsForMonth(
                    monthKey = firstDayOfWeek,
                    distance = session.distance,
                    duration = session.duration,
                    caloriesBurned = session.caloriesBurned,
                    pace = session.pace,
                    sessionSize = sessionSize
                )
            }
        }
    }

    fun calcStatsForYear() {
        viewModelScope.launch {
            val firstDayOfWeekInMonth = DateTimeUtils.getEveryFirstDayOfWeekInCurrentMonth()
            val currentYear = DateTimeUtils.getCurrentDate().year.toString()
            val sessionSize = firstDayOfWeekInMonth.size

            statsRepository.monthlyStatsMap.collect { monthlyStat ->
                for (day in firstDayOfWeekInMonth) {
                    val statsForWeek = monthlyStat[day]
                    if (statsForWeek != null) {
                        statsRepository.addStatsForYear(
                            yearKey = currentYear,
                            distance = statsForWeek.totalDistance ?: 0.0,
                            duration = statsForWeek.totalDuration ?: 0L,
                            caloriesBurned = statsForWeek.totalCaloriesBurned ?: 0.0,
                            pace = statsForWeek.totalAvgPace ?: 0.0,
                            sessionSize = sessionSize + 1
                        )
                    }
                }
            }
        }
    }
}