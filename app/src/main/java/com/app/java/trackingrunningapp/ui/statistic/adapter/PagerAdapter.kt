package com.app.java.trackingrunningapp.ui.statistic.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.java.trackingrunningapp.ui.statistic.fragment.period.StatisticMonthFragment
import com.app.java.trackingrunningapp.ui.statistic.fragment.period.StatisticTodayFragment
import com.app.java.trackingrunningapp.ui.statistic.fragment.period.StatisticWeekFragment

class PagerAdapter(
    private val fm: Fragment
) : FragmentStateAdapter(fm) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> StatisticTodayFragment()
            1 -> StatisticWeekFragment()
            2 -> StatisticMonthFragment()
            else -> StatisticTodayFragment()
        }
    }
}