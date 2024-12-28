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
import com.app.java.trackingrunningapp.databinding.FragmentStatisticYearlyBinding
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModelFactory
import com.db.williamchart.view.BarChartView
import com.google.android.material.tabs.TabLayout


class StatisticYearlyFragment : Fragment() {
    private lateinit var binding:FragmentStatisticYearlyBinding
    private lateinit var runSessionViewModel: RunSessionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatisticYearlyBinding.inflate(inflater,container,false)

        val runFactory = RunSessionViewModelFactory(InitDatabase.runSessionRepository)
        runSessionViewModel =
            ViewModelProvider(this, runFactory).get(RunSessionViewModel::class.java)

        val startDate = "20241123"
        val endDate = "20241223"
        runSessionViewModel.filterSessionsByDateRange(startDate, endDate)

        val yearData: MutableList<Double> = mutableListOf()
        val yearSums: MutableList<Double> = mutableListOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        runSessionViewModel.filteredSession.observe(viewLifecycleOwner) { sessions ->
            if (sessions.isNotEmpty()) {
                for (session in sessions) {
                    val date = session.runDate
                    val month = date[4].toString().toInt() * 10 + date[5].toString().toInt()
                    yearSums[month-1] += session.distance ?: 0.0
                }
                for(yearSum in yearSums) {
                    yearData.add(yearSum)
                }
                Log.d("Data by year: ", "$yearData")
                setupBarChart(yearData)
            } else {
                Log.e("Error", "No session found in given dates")
            }
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    private fun setupBarChart(monthData: MutableList<Double>) {
        val barSet = listOf(
            "1" to monthData[0].toFloat(),
            "2" to monthData[1].toFloat(),
            "3" to monthData[2].toFloat(),
            "4" to monthData[3].toFloat(),
            "5" to monthData[4].toFloat(),
            "6" to monthData[5].toFloat(),
            "7" to monthData[6].toFloat(),
            "8" to monthData[7].toFloat(),
            "9" to monthData[8].toFloat(),
            "10" to monthData[9].toFloat(),
            "11" to monthData[10].toFloat(),
            "12" to monthData[11].toFloat(),
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