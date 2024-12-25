package com.app.java.trackingrunningapp.ui.statistic.fragment.period

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.databinding.FragmentStatisticWeeklyBinding
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModelFactory
import com.db.williamchart.view.BarChartView
import com.google.android.material.tabs.TabLayout

class StatisticMonthlyFragment : Fragment() {
    private lateinit var binding: FragmentStatisticWeeklyBinding
    private lateinit var runSessionViewModel: RunSessionViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatisticWeeklyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewmodel()
    }

    private fun setupViewmodel() {
        val runFactory = RunSessionViewModelFactory(InitDatabase.runSessionRepository)
        runSessionViewModel =
            ViewModelProvider(this, runFactory).get(RunSessionViewModel::class.java)
        val startDate = "20241123"
        val endDate = "20241222"

        runSessionViewModel.filterSessionsByDateRange(startDate, endDate)

        val monthData: MutableList<Double> = mutableListOf()
        val endWeeks: List<String> = listOf("20241129","20241206","20241212","20241218", "20241224")

        runSessionViewModel.filteredSession.observe(viewLifecycleOwner) { sessions ->
            if (sessions.isNotEmpty()) {
                var sumDay = 0.0
                var week = 0
                for (session in sessions) {
                    sumDay += session.distance
                    if (session.runDate > endWeeks[week]) {
                        monthData.add(sumDay)
                        sumDay = 0.0
                        week++
                    }
                }
                Log.d("Data by month: ", "$monthData")
                setupBarChart(monthData)
            } else {
                Log.e("Error", "No session found in given dates")
            }
        }
    }


    private fun setupBarChart(monthData: MutableList<Double>) {
        val barSet = listOf(
            "1" to monthData[0].toFloat(),
            "2" to monthData[1].toFloat(),
            "3" to monthData[2].toFloat(),
            "4" to monthData[3].toFloat()
        )
        val barChart = binding.barchart
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