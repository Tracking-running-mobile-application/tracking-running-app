package com.app.java.trackingrunningapp.ui.statistic

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

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