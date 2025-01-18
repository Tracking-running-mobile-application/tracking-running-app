package com.app.java.trackingrunningapp.ui.statistic.fragment.period

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.model.entity.YearlyStats
import com.app.java.trackingrunningapp.databinding.FragmentStatisticYearlyBinding
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModelFactory
import com.app.java.trackingrunningapp.ui.viewmodel.StatsViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.StatsViewModelFactory
import com.db.williamchart.view.BarChartView
import com.google.android.material.tabs.TabLayout


class StatisticYearlyFragment : Fragment() {
    private lateinit var binding:FragmentStatisticYearlyBinding
    private lateinit var runSessionViewModel: RunSessionViewModel
    private lateinit var statsViewModel: StatsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatisticYearlyBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewmodel()
    }

    private fun setupViewmodel() {
        val statsFactory = StatsViewModelFactory(InitDatabase.statsRepository)
        statsViewModel =
            ViewModelProvider(requireActivity(), statsFactory)[StatsViewModel::class.java]
        statsViewModel.refreshStats()
        statsViewModel.currentYearStats.observe(viewLifecycleOwner) { sessions ->
            Log.d("current_year", "${sessions}")
            statsViewModel.refreshStats()
            setupBarChart(sessions)
        }
    }


    private fun setupBarChart(monthData: List<YearlyStats>) {
        val barSet = listOf(
            "1" to monthData[0].totalDistance!!.toFloat(),
            "2" to monthData[1].totalDistance!!.toFloat(),
            "3" to monthData[2].totalDistance!!.toFloat(),
            "4" to monthData[3].totalDistance!!.toFloat(),
            "5" to monthData[4].totalDistance!!.toFloat(),
            "6" to monthData[5].totalDistance!!.toFloat(),
            "7" to monthData[6].totalDistance!!.toFloat(),
            "8" to monthData[7].totalDistance!!.toFloat(),
            "9" to monthData[8].totalDistance!!.toFloat(),
            "10" to monthData[9].totalDistance!!.toFloat(),
            "11" to monthData[10].totalDistance!!.toFloat(),
            "12" to monthData[11].totalDistance!!.toFloat(),
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