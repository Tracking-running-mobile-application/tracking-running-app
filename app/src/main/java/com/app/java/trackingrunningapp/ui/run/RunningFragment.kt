package com.app.java.trackingrunningapp.ui.run

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.databinding.FragmentRunningBinding
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.AnnotationPlugin
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPolylineAnnotationManager
import com.mapbox.maps.plugin.locationcomponent.location


class RunningFragment : Fragment() {
    private lateinit var binding:FragmentRunningBinding
    private var isPaused:Boolean = true
    private lateinit var mapView: MapView
    private val routeCoordinates = mutableListOf<Point>()
    private lateinit var annotationApi: AnnotationPlugin
    private lateinit var polylineAnnotationManager: PolylineAnnotationManager
    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allGranted = permissions.all {
                it.value
            }
            if (allGranted) {
                initMapAndLocation()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permissions are required to proceed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRunningBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArrowAction()
        setupPermission()
        setupActionRun()
    }

    private fun setupActionRun() {
        binding.btnPauseAndResume.setOnClickListener {
            if(isPaused){
                binding.btnPauseAndResume.text = "Pause"
                // TODO: do something when continue
                isPaused = false
            }else{
                binding.btnPauseAndResume.text = "Resume"
                // TODO: do something when pause
                isPaused = true
            }
        }
        binding.btnStopTracking.setOnClickListener {
            binding.btnPauseAndResume.visibility = View.GONE
            isPaused = true
            binding.btnStopTracking.visibility = View.GONE
            // TODO: Do something when stop
            it.findNavController().navigate(R.id.action_runningFragment_to_runResultFragment)
        }
    }

    @SuppressLint("InlinedApi")
    private fun setupPermission() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS
        )
        // Check if all permissions are granted
        if (permissions.all {// false if 1 permission is denied
                ContextCompat
                    .checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
            }) {
            initMapAndLocation()
        } else {
            // Request missing permissions
            requestPermissionsLauncher.launch(permissions)
        }
    }

    private fun initArrowAction() {
        binding.icArrowUp.setOnClickListener {
            binding.icArrowDown.visibility = View.VISIBLE
            binding.icArrowUp.visibility = View.GONE
            binding.layoutMetric.root.visibility = View.GONE
        }
        binding.icArrowDown.setOnClickListener {
            binding.icArrowUp.visibility = View.VISIBLE
            binding.icArrowDown.visibility = View.GONE
            binding.layoutMetric.root.visibility = View.VISIBLE
        }
    }

    private fun initMapAndLocation() {
        // Setup rout drawing
        mapView = binding.mapView
        annotationApi = mapView.annotations
        polylineAnnotationManager = annotationApi.createPolylineAnnotationManager()
        // Setup map
        mapView.mapboxMap.loadStyle(Style.STANDARD) {
            // Init location
            val locationComponentPlugin = mapView.location
            locationComponentPlugin.updateSettings {
                this.enabled = true
            }
            locationComponentPlugin.addOnIndicatorPositionChangedListener { point ->
                routeCoordinates.add(point)
                drawRoute()
                mapView.mapboxMap.setCamera(
                    CameraOptions.Builder().center(point).zoom(15.0).build()
                )
            }
        }
    }

    private fun drawRoute() {
        polylineAnnotationManager.deleteAll()
        val polylineAnnotationOptions: PolylineAnnotationOptions = PolylineAnnotationOptions()
            .withPoints(routeCoordinates)
            .withLineColor("#FF0000")
            .withLineWidth(5.0)
        polylineAnnotationManager.create(polylineAnnotationOptions)
    }

}