package com.app.java.trackingrunningapp.ui.run_page

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.ui.FusedLocationAPI.DefaultLocationClient
import com.app.java.trackingrunningapp.ui.FusedLocationAPI.LocationService

class RunPageFragment : Fragment(R.layout.fragment_run_page) {
    private var locationClient: DefaultLocationClient? = null
    private var isOverlayVisible = true
    private var isTracking = false

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

        val toggleButton = view.findViewById<ImageView>(R.id.toggleButton)
        val metricsRecyclerView = view.findViewById<RecyclerView>(R.id.metricsRecyclerView)
        metricsRecyclerView.visibility = View.VISIBLE

        toggleButton.setOnClickListener { v: View? ->
            if (isOverlayVisible) {
                metricsRecyclerView.visibility = View.GONE
                toggleButton.rotation = 180f
            } else {
                metricsRecyclerView.visibility = View.VISIBLE
                toggleButton.rotation = 0f
            }
            isOverlayVisible = !isOverlayVisible
        }

        val metricItems: MutableList<MetricItem> = ArrayList()
        metricItems.add(MetricItem("Duration", "01:25:40"))
        metricItems.add(MetricItem("Distance", "10.02 km"))
        metricItems.add(MetricItem("Pace", "9:12 / km"))
        metricItems.add(MetricItem("Calories", "220 kcal"))

        metricsRecyclerView.layoutManager = GridLayoutManager(getContext(), 2)
        val adapter = MetricItemAdapter(metricItems)
        metricsRecyclerView.adapter = adapter

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
}