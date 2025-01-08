package com.app.java.trackingrunningapp.ui.statistic.fragment.period

import android.os.Bundle
import android.util.Log
import android.util.LongSparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.model.entity.MonthlyStats
import com.app.java.trackingrunningapp.data.model.entity.YearlyStats
import com.app.java.trackingrunningapp.databinding.FragmentStatisticWeeklyBinding
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModelFactory
import com.app.java.trackingrunningapp.ui.viewmodel.StatsViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.StatsViewModelFactory
import com.db.williamchart.view.BarChartView
import com.google.android.material.tabs.TabLayout

class StatisticMonthlyFragment : Fragment() {
    private lateinit var binding: FragmentStatisticWeeklyBinding
    private lateinit var runSessionViewModel: RunSessionViewModel
    private lateinit var statsViewModel: StatsViewModel
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
//        val runFactory = RunSessionViewModelFactory(InitDatabase.runSessionRepository)
//        runSessionViewModel =
//            ViewModelProvider(this, runFactory).get(RunSessionViewModel::class.java)
//        val startDate = "20241123"
//        val endDate = "20241222"
//
//        runSessionViewModel.filterSessionsByDateRange(startDate, endDate)
        val statsFactory = StatsViewModelFactory(InitDatabase.statsRepository)
        statsViewModel =
            ViewModelProvider(requireActivity(), statsFactory)[StatsViewModel::class.java]
//        val weekData: MutableList<Double> = mutableListOf()
//        val days: List<String> = getPreviousDays(6).reversed()
//        val daySums: MutableList<Double> = mutableListOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        statsViewModel.currentYearStats.observe(viewLifecycleOwner) { sessions ->
            Log.d("current_year","${sessions}")
            setupBarChart(sessions)
        }
        statsViewModel.refreshStats()
    }
    private fun setupBarChart(monthData: List<YearlyStats>) {
        val barSet = listOf(
            "1" to monthData[0].totalDistance!!.toFloat(),
            "2" to monthData[1].totalDistance!!.toFloat(),
            "3" to monthData[2].totalDistance!!.toFloat(),
            "4" to monthData[3].totalDistance!!.toFloat()
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