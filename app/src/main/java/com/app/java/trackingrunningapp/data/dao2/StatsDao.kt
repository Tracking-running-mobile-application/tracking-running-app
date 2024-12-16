package com.app.java.trackingrunningapp.data.dao2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.app.java.trackingrunningapp.data.model.entity.stat.MonthlyStats
import com.app.java.trackingrunningapp.data.model.entity.stat.WeeklyStats
import com.app.java.trackingrunningapp.data.model.entity.stat.YearlyStats

@Dao
interface StatsDao {
    @Query("SELECT * FROM MonthlyStats WHERE month_stats_key = :month")
    suspend fun getMonthlyStats(month: String): MonthlyStats?

    @Update
    suspend fun updateMonthlyStats(monthlyStats: MonthlyStats)

    @Insert
    suspend fun insertMonthlyStats(monthlyStats: MonthlyStats)

    @Query("SELECT * FROM WeeklyStats WHERE week_stats_key = :week")
    suspend fun getWeeklyStats(week: String): WeeklyStats?

    @Update
    suspend fun updateWeeklyStats(weeklyStats: WeeklyStats)
    @Insert
    suspend fun insertWeeklyStats(weeklyStats: WeeklyStats)

    @Query("SELECT * FROM YearlyStats WHERE year_stats_key = :year")
    suspend fun getYearlyStats(year: String): YearlyStats?
    @Update
    suspend fun updateYearlyStats(yearlyStats: YearlyStats)
    @Insert
    suspend fun insertYearlyStats(yearlyStats: YearlyStats)

}