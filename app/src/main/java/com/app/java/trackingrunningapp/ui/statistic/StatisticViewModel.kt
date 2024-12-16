package com.app.java.trackingrunningapp.ui.statistic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.data.model.entity.stat.MonthlyStats
import com.app.java.trackingrunningapp.data.model.entity.stat.WeeklyStats
import com.app.java.trackingrunningapp.data.model.entity.stat.YearlyStats
import com.app.java.trackingrunningapp.data.repository.StatsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StatisticViewModel(
    private val repository: StatsRepository
) : ViewModel() {
    private val _weeks = MutableLiveData<WeeklyStats>()
    val weeks: LiveData<WeeklyStats> = _weeks

    private val _months = MutableLiveData<MonthlyStats>()
    val months: LiveData<MonthlyStats> = _months

    private val _years = MutableLiveData<YearlyStats>()
    val years: LiveData<YearlyStats> = _years

    /**
     * GET
     */
    fun getMonthly(month: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getMonthlyStats(month)
        }
    }
    fun getWeekly(week: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getWeeklyStats(week)
        }
    }
    fun getYearly(year: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getYearlyStats(year)
        }
    }

    /**
     * INSERT
     */

    fun insertMonthly(month: MonthlyStats) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertMonthlyStats(month)
        }
    }
    fun insertWeekly(week: WeeklyStats) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertWeeklyStats(week)
        }
    }
    fun insertYearly(year: YearlyStats) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertYearlyStats(year)
        }
    }

    /**
     * UPDATE
     */

    fun updateMonthly(month: MonthlyStats) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMonthlyStats(month)
        }
    }
    fun updateWeekly(week: WeeklyStats) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateWeeklyStats(week)
        }
    }
    fun updateYearly(year: YearlyStats) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateYearlyStats(year)
        }
    }

    class Factory(
        private val repository: StatsRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StatisticViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return StatisticViewModel(repository) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel")
            }
        }
    }
}