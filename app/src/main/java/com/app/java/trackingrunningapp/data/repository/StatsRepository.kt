package com.app.java.trackingrunningapp.data.repository

import com.app.java.trackingrunningapp.data.dao2.StatsDao
import com.app.java.trackingrunningapp.data.model.entity.stat.MonthlyStats
import com.app.java.trackingrunningapp.data.model.entity.stat.WeeklyStats
import com.app.java.trackingrunningapp.data.model.entity.stat.YearlyStats

class StatsRepository(
    private val statsDao: StatsDao
) {
    suspend fun getMonthlyStats(month: String): MonthlyStats? {
        return statsDao.getMonthlyStats(month)
    }

    suspend fun updateMonthlyStats(monthlyStats: MonthlyStats) {
        statsDao.updateMonthlyStats(monthlyStats)
    }

    suspend fun insertMonthlyStats(monthlyStats: MonthlyStats){
        statsDao.insertMonthlyStats(monthlyStats)
    }
    suspend fun getWeeklyStats(week: String): WeeklyStats? {
        return statsDao.getWeeklyStats(week)
    }

    suspend fun updateWeeklyStats(weeklyStats: WeeklyStats) {
        statsDao.updateWeeklyStats(weeklyStats)
    }
    suspend fun insertWeeklyStats(weeklyStats: WeeklyStats){
        statsDao.insertWeeklyStats(weeklyStats)
    }

    suspend fun getYearlyStats(year: String): YearlyStats? {
        return statsDao.getYearlyStats(year)
    }

    suspend fun updateYearlyStats(yearlyStats: YearlyStats) {
        return statsDao.updateYearlyStats(yearlyStats)
    }
    suspend fun insertYearlyStats(yearlyStats: YearlyStats){
        statsDao.insertYearlyStats(yearlyStats)
    }
}