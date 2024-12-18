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
import com.app.java.trackingrunningapp.databinding.FragmentStatisticWeeklyBinding
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModelFactory
import com.app.java.trackingrunningapp.utils.DateTimeUtils
import com.app.java.trackingrunningapp.utils.LocalTimeConverter
import com.google.android.material.tabs.TabLayout
import com.mapbox.maps.extension.style.expressions.dsl.generated.sum
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class StatisticWeeklyFragment : Fragment() {
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

        val weekData: MutableList<Double> = mutableListOf()
        val days: List<String> = getPreviousDays(6).reversed()
        val daySums: MutableList<Double> = mutableListOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        runSessionViewModel.filteredSession.observe(viewLifecycleOwner) { sessions ->
            if (sessions.isNotEmpty()) {
                for (session in sessions) {
                    val idx = days.indexOf(session.runDate)
                    if(idx != -1) {
                        daySums[idx] += session.distance
                    }
                }
                for (daySum in daySums) {
                    weekData.add(daySum)
                }
                Log.d("Data by week: ", "$weekData")
                setupBarChart(weekData)
            } else {
                Log.e("Error", "No session found in given dates")
            }
        }
    }

    private fun setupBarChart(weekData: MutableList<Double>) {
        val barSet = listOf(
            "Mon" to weekData[0].toFloat(),
            "Tue" to weekData[1].toFloat(),
            "Wed" to weekData[2].toFloat(),
            "Thu" to weekData[3].toFloat(),
            "Fri" to weekData[4].toFloat(),
            "Sat" to weekData[5].toFloat(),
            "Sun" to weekData[6].toFloat(),


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
    fun getPreviousDays(currentDayIndex: Int): List<String> {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")

        // Calculate the previous days
        return (currentDayIndex downTo 0).map { offset ->
            today.minusDays((currentDayIndex - offset).toLong()).format(formatter)
        }
    }
}