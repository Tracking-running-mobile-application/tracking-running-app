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
import com.app.java.trackingrunningapp.databinding.FragmentStatisticMonthBinding
import com.app.java.trackingrunningapp.ui.viewmodel.GPSTrackViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.GPSTrackViewModelFactory
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModelFactory
import com.db.williamchart.view.BarChartView
import com.google.android.material.tabs.TabLayout


class StatisticMonthFragment : Fragment() {
    private lateinit var binding:FragmentStatisticMonthBinding
    private lateinit var runSessionViewModel: RunSessionViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatisticMonthBinding.inflate(inflater,container,false)

        val runFactory = RunSessionViewModelFactory(InitDatabase.runSessionRepository)
        runSessionViewModel = ViewModelProvider(this, runFactory).get(RunSessionViewModel::class.java)

        val startDate = "20241123"
        val endDate = "20241222"
        runSessionViewModel.filterSessionsByDateRange(startDate, endDate)

        runSessionViewModel.filteredSession.observe(viewLifecycleOwner) {
            sessions ->
            if (sessions.isNotEmpty()) {
                for (session in sessions) {
                    Log.d("Filtered Session", "${session.sessionId}, ${session.distance}, ${session.runDate}")
                }
            }
            else {
                Log.e("Error", "No session found in given dates")
            }
        }

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val barSet = listOf(
            "1" to 4F,
            "2" to 2F,
            "3" to 4.5F,
            "4" to 3F,
            "5" to 6F,
            "6" to 1.5F,
            "7" to 4F,
            "8" to 22F,
            "9" to 5f,
            "10" to 2f,
            "11" to 3f,
            "12" to 11f,
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