package com.app.java.trackingrunningapp.ui.statistic.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.java.trackingrunningapp.ui.statistic.fragment.period.StatisticYearlyFragment
import com.app.java.trackingrunningapp.ui.statistic.fragment.period.StatisticWeeklyFragment
import com.app.java.trackingrunningapp.ui.statistic.fragment.period.StatisticMonthlyFragment

class PagerAdapter(
    private val fm: Fragment
) : FragmentStateAdapter(fm) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> StatisticWeeklyFragment()
            1 -> StatisticMonthlyFragment()
            2 -> StatisticYearlyFragment()
            else -> StatisticWeeklyFragment()
        }
    }
}