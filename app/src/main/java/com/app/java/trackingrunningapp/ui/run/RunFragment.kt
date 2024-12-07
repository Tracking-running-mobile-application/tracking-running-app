package com.app.java.trackingrunningapp.ui.run

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.databinding.FragmentRunBinding
import com.app.java.trackingrunningapp.ui.FusedLocationAPI.DefaultLocationClient
import com.app.java.trackingrunningapp.ui.FusedLocationAPI.LocationService

class RunFragment : Fragment() {
    private lateinit var binding: FragmentRunBinding
    private var locationClient: DefaultLocationClient? = null
    private var isOverlayVisible = true
    private var isTracking = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRunBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.POST_NOTIFICATIONS
            ),
            0
        )

        val toggleTrackingButton: Button = view.findViewById(R.id.toggleTrackingButton)
        toggleTrackingButton.text = "Start Tracking" // Initial text

        toggleTrackingButton.setOnClickListener {
            if (isTracking) {
                stopTracking()
                toggleTrackingButton.text = "Start Tracking"
            } else {
                startTracking()
                toggleTrackingButton.text = "Stop Tracking"
            }
            isTracking = !isTracking
        }
        binding.layoutMetric.root.visibility = View.VISIBLE
        // set action arrow
        binding.icArrowUp.setOnClickListener {
            binding.layoutMetric.root.visibility = View.GONE
            binding.icArrowUp.visibility = View.GONE
            binding.icArrowDown.visibility = View.VISIBLE
        }
        binding.icArrowDown.setOnClickListener {
            binding.icArrowUp.visibility = View.VISIBLE
            binding.layoutMetric.root.visibility = View.VISIBLE
            binding.icArrowDown.visibility = View.GONE
        }
    }

    private fun startTracking() {
        val context = requireContext()
        val intent = Intent(context, LocationService::class.java).apply {
            action = LocationService.ACTION_START
        }
        context.startService(intent)
    }

    private fun stopTracking() {
        val context = requireContext()
        val intent = Intent(context, LocationService::class.java).apply {
            action = LocationService.ACTION_STOP
        }
        context.startService(intent)
    }

    override fun onStop() {
        super.onStop()
        binding.layoutMetric.root.visibility = View.VISIBLE
    }
}