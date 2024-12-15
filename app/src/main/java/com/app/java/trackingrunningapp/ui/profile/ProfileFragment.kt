package com.app.java.trackingrunningapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.databinding.FragmentProfileBinding

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
        setupBarChart()
         //pop to profile
//        this.findNavController().popBackStack(R.id.profileFragment, false)
//        this.findNavController().navigate(R.id.action_global_profileFragment)
        navigateToFavourite()
    }

    private fun navigateToFavourite() {
        binding.cvFavoriteRun.setOnClickListener{
            it.findNavController().navigate(R.id.action_profileFragment_to_noFavouriteFragment)
        }
    }

    private fun setupBarChart() {
        val barSet = listOf(
            "Mon" to 4F,
            "Tue" to 2F,
            "Wed" to 4.5F,
            "Thu" to 3F,
            "Fri" to 6F,
            "Sat" to 1.5F,
            "Sun" to 4F
        )
        val barChart = binding.barchart
        barChart.animate(barSet)
        barChart.apply {
            animation.duration = 1000L
            labelsFormatter = { it.toInt().toString() }
        }
    }
}