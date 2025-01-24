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
import com.app.java.trackingrunningapp.data.model.entity.WeeklyStats
import com.app.java.trackingrunningapp.databinding.FragmentStatisticWeeklyBinding
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModelFactory
import com.app.java.trackingrunningapp.ui.viewmodel.StatsViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.StatsViewModelFactory
import com.app.java.trackingrunningapp.utils.DateTimeUtils
import com.google.android.material.tabs.TabLayout
import com.mapbox.maps.extension.style.expressions.dsl.generated.sum
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class StatisticWeeklyFragment : Fragment() {
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
        val statsFactory = StatsViewModelFactory(InitDatabase.statsRepository)
        statsViewModel =
            ViewModelProvider(requireActivity(), statsFactory)[StatsViewModel::class.java]
        statsViewModel.refreshStats()
        statsViewModel.currentWeekStats.observe(viewLifecycleOwner) { sessions ->
            Log.d("current_week","${sessions}")
            setupBarChart(sessions)
            statsViewModel.refreshStats()
        }
    }



    private fun setupBarChart(weekData: List<WeeklyStats>) {
        val barSet = listOf(
            getString(R.string.mon) to weekData[0].totalDistance!!.toFloat(),
            getString(R.string.tue) to weekData[1].totalDistance!!.toFloat(),
            getString(R.string.wed) to weekData[2].totalDistance!!.toFloat(),
            getString(R.string.thu) to weekData[3].totalDistance!!.toFloat(),
            getString(R.string.fri) to weekData[4].totalDistance!!.toFloat(),
            getString(R.string.sat) to weekData[5].totalDistance!!.toFloat(),
            getString(R.string.sun) to weekData[6].totalDistance!!.toFloat(),
        )
        val barChart = binding.barchart
        barChart.animate(barSet)
        barChart.apply {
            animation.duration = 1000L
            labelsFormatter = { getString(R.string.chart_label,it.toDouble())}
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