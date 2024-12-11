package com.app.java.trackingrunningapp.ui.run

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.ui.FusedLocationAPI.DefaultLocationClient
import com.app.java.trackingrunningapp.ui.FusedLocationAPI.LocationService
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location


class RunFragment : Fragment(R.layout.fragment_run) {
    private var locationClient: DefaultLocationClient? = null
    private var isOverlayVisible = true
    private var isTracking = false
    private lateinit var mapView: MapView
    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allGranted = permissions.all { it.value }
            if (allGranted) {
                initializeMapAndLocation()
            } else {
                Toast.makeText(requireContext(), "Permissions are required to proceed.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.mapView)

        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS
        )

        // Check if all permissions are granted
        if (permissions.all {
                ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
            }) {
            initializeMapAndLocation()
        } else {
            // Request missing permissions
            requestPermissionsLauncher.launch(permissions)
        }

        val toggleTrackingButton: Button = view.findViewById(R.id.toggleTrackingButton)

        toggleTrackingButton.setOnClickListener {
            if (isTracking) {
                stopTracking()
            } else {
                startTracking()
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

    private fun initializeMapAndLocation() {
        mapView.mapboxMap.loadStyle(Style.STANDARD) { style ->
            mapView.location.updateSettings {
                enabled = true
            }

            val positionChangedListener = OnIndicatorPositionChangedListener { point ->
                val cameraOptions = CameraOptions.Builder()
                    .center(point)
                    .zoom(15.0)
                    .build()
                mapView.mapboxMap.setCamera(cameraOptions)
            }

            mapView.location.addOnIndicatorPositionChangedListener(positionChangedListener)
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
}