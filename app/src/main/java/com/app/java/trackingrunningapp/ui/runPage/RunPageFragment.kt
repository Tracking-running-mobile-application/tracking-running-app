package com.app.java.trackingrunningapp.ui.runPage

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.ui.FusedLocationAPI.DefaultLocationClient
import com.app.java.trackingrunningapp.ui.FusedLocationAPI.LocationService
import com.google.android.gms.location.LocationServices

class RunPageFragment : Fragment(R.layout.fragment_run_page) {
    private var locationClient: DefaultLocationClient? = null
    private var isOverlayVisible = true
    private var isTracking = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireContext()
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        locationClient = DefaultLocationClient(context, fusedLocationProviderClient)

        val toggleTrackingButton = view.findViewById<Button>(R.id.toggleTrackingButton)
        toggleTrackingButton.text = "Start Running Session"

        toggleTrackingButton.setOnClickListener { v: View? ->
            if (isTracking) {
                stopTracking()
                toggleTrackingButton.text = "Start Running Session"
            } else {
                if (locationClient!!.hasLocationPermission()) {
                    startTracking()
                    toggleTrackingButton.text = "Pause Running Session"
                } else {
                    locationClient!!.requestLocationPermissions(requireActivity())
                }
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

        if (!locationClient!!.hasLocationPermission()) {
            locationClient!!.requestLocationPermissions(requireActivity())
        }
    }

    private fun startTracking() {
        val context = requireContext()
        val intent = Intent(context, LocationService::class.java)
        intent.setAction(LocationService.ACTION_START)
        context.startService(intent)
    }

    private fun stopTracking() {
        val context = requireContext()
        val intent = Intent(context, LocationService::class.java)
        intent.setAction(LocationService.ACTION_STOP)
        context.startService(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == DefaultLocationClient.LOCATION_PERMISSION_REQUEST_CODE) {
            if (locationClient!!.hasLocationPermission()) {
                startTracking()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Location permission is required to track location",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}