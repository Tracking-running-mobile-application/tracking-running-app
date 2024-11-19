package com.app.java.trackingrunningapp.ui.statistic.fragment.period

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.databinding.FragmentStatisticWeekBinding
import com.db.williamchart.view.BarChartView
import com.google.android.material.tabs.TabLayout

class StatisticWeekFragment : Fragment() {
    private lateinit var binding: FragmentStatisticWeekBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatisticWeekBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val barSet = listOf(
            "Week 1" to 4F,
            "Week 2" to 2F,
            "Week 3" to 4.5F,
            "Week 4" to 3F,
            "Week 5" to 9F,
            "Week 6" to 13F,
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