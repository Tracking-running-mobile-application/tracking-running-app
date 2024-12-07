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
import com.app.java.trackingrunningapp.databinding.FragmentRunBinding
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location


class RunFragment : Fragment() {
    private lateinit var binding: FragmentRunBinding
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


    override fun onStop() {
        super.onStop()
        binding.layoutMetric.root.visibility = View.VISIBLE
    }
}