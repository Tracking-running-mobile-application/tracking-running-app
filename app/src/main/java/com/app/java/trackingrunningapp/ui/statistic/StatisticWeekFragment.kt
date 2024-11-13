package com.app.java.trackingrunningapp.ui.statistic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.databinding.FragmentStatisticWeekBinding
import com.db.williamchart.view.BarChartView

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
            "M" to 4F,
            "T" to 2F,
            "W" to 4.5F,
            "T" to 3F,
            "F" to 6F,
            "S" to 1.5F,
            "S" to 4F
        )
        val barChart = view.findViewById<BarChartView>(R.id.barchart)
        barChart.apply {
            animation.duration = 1000L
            labelsFormatter = {it.toInt().toString()}
        }
        barChart.animate(barSet)
    }
}