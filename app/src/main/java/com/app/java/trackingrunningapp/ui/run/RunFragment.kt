package com.app.java.trackingrunningapp.ui.run

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.model.entity.User
import com.app.java.trackingrunningapp.data.repository.UserRepository
import com.app.java.trackingrunningapp.databinding.FragmentRunBinding
import com.app.java.trackingrunningapp.ui.viewmodel.GPSPointViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.GPSPointViewModelFactory
import com.app.java.trackingrunningapp.ui.viewmodel.GPSTrackViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.GPSTrackViewModelFactory
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModelFactory
import com.app.java.trackingrunningapp.ui.viewmodel.UserViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.UserViewModelFactory
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

enum class TrackingState {
    RUNNING,
    PAUSED,
    STOPPED,
}

class RunFragment : Fragment() {
    private lateinit var binding: FragmentRunBinding
    private var trackingState: TrackingState = TrackingState.STOPPED
    private var lastUpdateTime: Long = 0L
    private val updateInterval: Long = 1000L
    private lateinit var mapView: MapView
    private val routeCoordinates = mutableListOf<Point>()
    private lateinit var annotationApi: AnnotationPlugin
    private lateinit var polylineAnnotationManager: PolylineAnnotationManager
    private lateinit var runSessionViewModel: RunSessionViewModel
    private lateinit var gpsTrackViewModel: GPSTrackViewModel
    private lateinit var gpsPointViewModel: GPSPointViewModel
    private lateinit var userViewModel: UserViewModel

    private var mutex = Mutex()

    private var indicatorListener: OnIndicatorPositionChangedListener? = null

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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val runFactory = RunSessionViewModelFactory(InitDatabase.runSessionRepository)
        runSessionViewModel =
            ViewModelProvider(this, runFactory).get(RunSessionViewModel::class.java)

        val gpsTrackFactory = GPSTrackViewModelFactory(InitDatabase.gpsTrackRepository)
        gpsTrackViewModel =
            ViewModelProvider(this, gpsTrackFactory).get(GPSTrackViewModel::class.java)

        val gpsPointFactory = GPSPointViewModelFactory(InitDatabase.gpsPointRepository)
        gpsPointViewModel =
            ViewModelProvider(this, gpsPointFactory).get(GPSPointViewModel::class.java)

        val userRepository = UserRepository()
        val userFactory = UserViewModelFactory(userRepository)
        userViewModel = ViewModelProvider(this, userFactory)[UserViewModel::class.java]
        binding = FragmentRunBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPermission()
        indicatorListener = OnIndicatorPositionChangedListener { point ->
            val currentTime = System.currentTimeMillis()

            if (trackingState == TrackingState.RUNNING && currentTime - lastUpdateTime >= updateInterval) {
                lastUpdateTime = currentTime
                routeCoordinates.add(point)

                // Save the point to the database
                Log.d("Longitude", point.longitude().toString())
                Log.d("Latitude", point.latitude().toString())
                lifecycleScope.launch {
                    gpsPointViewModel.insertGPSPoint(point.longitude(), point.latitude())
                }

                // Draw the route
                drawRoute()
            } else if (trackingState == TrackingState.PAUSED) {
                Log.d("Tracking", "Paused. Ignoring location updates.")
            }
        }
        setupActionRun()
        initArrowAction()
    }

    private fun setupActionRun() {
        binding.btnStartTracking.setOnClickListener {
            binding.btnStartTracking.visibility = View.INVISIBLE
            binding.btnPause.visibility = View.VISIBLE
            binding.btnStop.visibility = View.VISIBLE
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).isVisible = false

            // TODO: START
            lifecycleScope.launch {
                mutex.withLock {
                    runSessionViewModel.initiateRunSession()
                    gpsTrackViewModel.initiateGPSTrack()
                    runSessionViewModel.setRunSessionStartTime()
                    // TODO: insert start tracking and sending gps function
                    startTracking()
                    runSessionViewModel.fetchAndUpdateStats()
                }
            }
        }

        binding.btnPause.setOnClickListener {
            binding.btnPause.visibility = View.INVISIBLE
            binding.btnResume.visibility = View.VISIBLE
            // TODO: PAUSE
            lifecycleScope.launch {
                mutex.withLock {
                    // TODO: do something when pause
                    runSessionViewModel.pauseRunSession()
                    pauseTracking()
                    gpsTrackViewModel.stopGPSTrack()
                }
            }
        }

        binding.btnResume.setOnClickListener {
            binding.btnResume.visibility = View.INVISIBLE
            binding.btnPause.visibility = View.VISIBLE
            // TODO: RESUME
            lifecycleScope.launch {
                mutex.withLock {
                    // TODO: do something when resume
                    runSessionViewModel.setRunSessionStartTime()
                    resumeTracking()
                    gpsTrackViewModel.resumeGPSTrack()
                    runSessionViewModel.fetchAndUpdateStats()
                }
            }
        }

        binding.btnStop.setOnClickListener {
            // TODO: STOP
            lifecycleScope.launch {
                mutex.withLock {
                    // TODO: stop gps tracking
                    runSessionViewModel.fetchAndUpdateStats()
                    gpsTrackViewModel.stopGPSTrack()
                    stopTracking()
                    runSessionViewModel.finishRunSession()
                }
            }
//            runSessionViewModel.fetchRunSessions()
            runSessionViewModel.runSessions.observe(viewLifecycleOwner) { sessions ->
                if(sessions.isNotEmpty()){
                    val runId = sessions[0].sessionId
                    Log.d("RunIDD", "$runId")
                    val bundle = Bundle().apply {
                        putInt(RunResultFragment.EXTRA_RUN_ID_RESULT, runId ?: 0)
                    }
                    // send current run session id to Run Result
                    it.findNavController()
                        .navigate(R.id.action_runFragment_to_runResultFragment, bundle)
                }else{
                    Log.d("EmptyList","$sessions")
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
                    .checkSelfPermission(
                        requireContext(),
                        it
                    ) == PackageManager.PERMISSION_GRANTED
            }) {
            initMapAndLocation()
        } else {
            // Request missing permissions
            requestPermissionsLauncher.launch(permissions)
        }
    }

    private fun initArrowAction() {
        binding.icArrowUp.setOnClickListener {
            binding.containerArrowDown.visibility = View.VISIBLE
            binding.containerArrowUp.visibility = View.GONE
            binding.containerMetric.visibility = View.GONE
        }
        binding.icArrowDown.setOnClickListener {
            binding.containerArrowUp.visibility = View.VISIBLE
            binding.containerArrowDown.visibility = View.GONE
            binding.containerMetric.visibility = View.VISIBLE
        }
        val runDuration = binding.layoutMetric.textRunDurationMetric
        val runDistance = binding.layoutMetric.textRunDistanceMetric
        val runPace = binding.layoutMetric.textRunPaceMetric
        val runCalo = binding.layoutMetric.textRunCaloMetric


        runSessionViewModel.statsFlow.observe(viewLifecycleOwner) {
            runDuration.text = getString(R.string.text_duration_metric, it?.duration ?: 0.0)
            userViewModel.fetchUserInfo()
            userViewModel.userLiveData.observe(viewLifecycleOwner) { user ->
                if (user?.unit == User.UNIT_KM) {
                    runDistance.text = getString(R.string.text_distance_metric, it?.distance ?: 0.0)
                } else if (user?.unit == User.UNIT_MILE) {
                    runDistance.text =
                        getString(R.string.text_distance_metric_mile, it?.distance ?: 0.0)
                }
            }

            runPace.text = getString(R.string.text_pace_metric, it?.pace ?: 0.0)
            runCalo.text = getString(R.string.text_calorie_metric, it?.caloriesBurned ?: 0.0)
        }
    }

    private fun initMapAndLocation() {
        // Setup route drawing
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

            // Listen for the first location update to zoom in but don't track yet
            locationComponentPlugin.addOnIndicatorPositionChangedListener(object :
                OnIndicatorPositionChangedListener {
                override fun onIndicatorPositionChanged(point: Point) {
                    // Zoom to the initial position
                    mapView.mapboxMap.setCamera(
                        CameraOptions.Builder()
                            .center(point) // Set initial camera position
                            .zoom(15.0) // Zoom level
                            .build()
                    )

                    // Remove the listener after the initial zoom
                    locationComponentPlugin.removeOnIndicatorPositionChangedListener(this)
                }
            })
        }
    }

    private fun startTracking() {
        if (trackingState == TrackingState.STOPPED) {
            trackingState = TrackingState.RUNNING

            routeCoordinates.clear() // Clear old route if restarting
            val locationComponentPlugin = mapView.location
            locationComponentPlugin.updateSettings {
                this.enabled = true // Enable location updates
            }

            indicatorListener?.let {
                locationComponentPlugin.addOnIndicatorPositionChangedListener(it)
            }
            Log.d("Tracking", "Started.")
        }
    }

    private fun pauseTracking() {
        trackingState = TrackingState.PAUSED
        Log.d("Tracking", "Paused.")
    }

    private fun resumeTracking() {
        trackingState = TrackingState.RUNNING
        Log.d("Tracking", "Resumed.")
    }

    private fun stopTracking() {
        if (trackingState != TrackingState.STOPPED) {
            trackingState = TrackingState.STOPPED

            // Remove the listener
            val locationComponentPlugin = mapView.location
            indicatorListener?.let {
                locationComponentPlugin.removeOnIndicatorPositionChangedListener(it)
            }
            indicatorListener = null

            routeCoordinates.clear() // Clear route data
            polylineAnnotationManager.deleteAll()
            Log.d("Tracking", "Stopped.")
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