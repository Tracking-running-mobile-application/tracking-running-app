package com.app.java.trackingrunningapp.ui.statistic.fragment.period

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.java.trackingrunningapp.R
import com.db.williamchart.view.BarChartView
import com.google.android.material.tabs.TabLayout

class StatisticTodayFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_statistic_today, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val barSet = listOf(
            "Mon" to 4F,
            "Tue" to 2F,
            "Wed" to 4.5F,
            "Thu" to 3F,
            "Fri" to 6F,
            "Sat" to 1.5F,
            "Sun" to 4F
        )
        val barChart = view.findViewById<BarChartView>(R.id.barchart)
        barChart.animate(barSet)
        barChart.apply {
            animation.duration = 1000L
            labelsFormatter = { it.toInt().toString() }
        }
        requireActivity().findViewById<TabLayout>(R.id.tab_layout_statistic)
            .addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    barChart.animate(barSet)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    barChart.animate(barSet)
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    barChart.animate(barSet)
                }
            })
    }

}