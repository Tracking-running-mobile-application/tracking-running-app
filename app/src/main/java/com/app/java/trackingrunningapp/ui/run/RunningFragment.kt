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
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.databinding.FragmentRunningBinding
import com.app.java.trackingrunningapp.ui.viewmodel.GPSTrackViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.GPSTrackViewModelFactory
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.AnnotationPlugin
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPolylineAnnotationManager
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


class RunningFragment : Fragment() {
    private lateinit var binding:FragmentRunningBinding
    private var isTracking: Boolean = false
    private var isPaused: Boolean = false
    private lateinit var mapView: MapView
    private val routeCoordinates = mutableListOf<Point>()
    private lateinit var annotationApi: AnnotationPlugin
    private lateinit var polylineAnnotationManager: PolylineAnnotationManager
    private lateinit var runSessionViewModel: RunSessionViewModel
    private lateinit var gpsTrackViewModel: GPSTrackViewModel
    private var indicatorListener: OnIndicatorPositionChangedListener? = null

    private var mutex = Mutex()

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
        val runFactory = RunSessionViewModelFactory(InitDatabase.runSessionRepository)
        runSessionViewModel = ViewModelProvider(this, runFactory).get(RunSessionViewModel::class.java)

        val gpsTrackFactory = GPSTrackViewModelFactory(InitDatabase.gpsTrackRepository)
        gpsTrackViewModel = ViewModelProvider(this, gpsTrackFactory).get(GPSTrackViewModel::class.java)

        binding = FragmentRunningBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).visibility = View.GONE
        initArrowAction()
        setupPermission()
        setupActionRun()
    }

    private fun setupActionRun() {
        binding.btnPause.setOnClickListener {
            binding.btnPause.visibility = View.INVISIBLE
            binding.btnResume.visibility = View.VISIBLE
            // TODO: PAUSE
            lifecycleScope.launch {
                mutex.withLock {
                    runSessionViewModel.fetchAndUpdateStats()
                    runSessionViewModel.pauseRunSession()
                    gpsTrackViewModel.stopGPSTrack()
                }
            }
        }
        binding.btnResume.setOnClickListener {
            binding.btnResume.visibility = View.GONE
            binding.btnPause.visibility = View.VISIBLE
            // TODO: PAUSE
            lifecycleScope.launch {
                mutex.withLock {
                    runSessionViewModel.setRunSessionStartTime()
                    runSessionViewModel.fetchAndUpdateStats()
                    gpsTrackViewModel.resumeGPSTrack()
                }
            }
        }
        binding.btnStopTracking.setOnClickListener {
            binding.btnResume.visibility = View.GONE
            isPaused = true
//            binding.btnStopTracking.visibility = View.INVISIBLE
            // TODO: Do something when stop
            it.findNavController().navigate(R.id.action_runningFragment_to_runResultFragment)
            lifecycleScope.launch {
                mutex.withLock {
                    runSessionViewModel.fetchAndUpdateStats()
                    gpsTrackViewModel.stopGPSTrack()
                    runSessionViewModel.finishRunSession()
                }
            }
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

    fun stopTracking() {
        if (isTracking) {
            isTracking = false

            // Remove the location listener
            val locationComponentPlugin = mapView.location

            indicatorListener?.let {
                locationComponentPlugin.removeOnIndicatorPositionChangedListener(it)
            }
            indicatorListener = null // Clear the reference

            // Clear the route from the map
            polylineAnnotationManager.deleteAll()

            // Clear the routeCoordinates list
            routeCoordinates.clear()
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