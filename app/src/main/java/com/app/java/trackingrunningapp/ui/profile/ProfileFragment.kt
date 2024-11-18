package com.app.java.trackingrunningapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.databinding.FragmentProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
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
    }
}

