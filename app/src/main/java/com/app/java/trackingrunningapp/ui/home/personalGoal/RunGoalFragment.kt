package com.app.java.trackingrunningapp.ui.home.personalGoal

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.model.entity.User
import com.app.java.trackingrunningapp.data.repository.UserRepository
import com.app.java.trackingrunningapp.databinding.FragmentRunGoalBinding
import com.app.java.trackingrunningapp.ui.viewmodel.GPSPointViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.GPSPointViewModelFactory
import com.app.java.trackingrunningapp.ui.viewmodel.GPSTrackViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.GPSTrackViewModelFactory
import com.app.java.trackingrunningapp.ui.viewmodel.PersonalGoalViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.PersonalGoalViewModelFactory
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


class RunGoalFragment : Fragment() {
    private lateinit var binding: FragmentRunGoalBinding
    private var isTracking: Boolean = false
    private lateinit var mapView: MapView
    private val routeCoordinates = mutableListOf<Point>()
    private lateinit var annotationApi: AnnotationPlugin
    private lateinit var polylineAnnotationManager: PolylineAnnotationManager
    private lateinit var runSessionViewModel: RunSessionViewModel
    private lateinit var gpsTrackViewModel: GPSTrackViewModel
    private lateinit var gpsPointViewModel: GPSPointViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var personalGoalViewModel: PersonalGoalViewModel

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

        val personalGoalFactory = PersonalGoalViewModelFactory(
            InitDatabase.personalGoalRepository,
            InitDatabase.runSessionRepository
        )
        personalGoalViewModel = ViewModelProvider(
            requireActivity(),
            personalGoalFactory
        )[PersonalGoalViewModel::class.java]
        binding = FragmentRunGoalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<TextView>(R.id.tv_toolbar_title).text = getString(R.string.personal_goal)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).isVisible = false
        setupPermission()
        setupActionRun()
        initArrowAction()
        initProgress()
    }

    private fun initProgress() {
        val goalId = arguments?.getInt(EXTRA_GOAL_ID, 0)
        // TODO: setup progress  
        personalGoalViewModel.goalProgress.observe(viewLifecycleOwner) { progress ->
            binding.progressBarGoal.progress = progress?.toInt() ?: 0
            binding.textRunPercent.text = getString(R.string.text_goal_progress,progress)
        }
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
                    Log.d("PersonalGoal", "1")
                    gpsTrackViewModel.initiateGPSTrack()
                    runSessionViewModel.setRunSessionStartTime()

                    // TODO: insert start tracking and sending gps function
                    startTracking()
                    Log.d("PersonalGoal", "2")
                    runSessionViewModel.fetchAndUpdateStats()
                    Log.d("PersonalGoal", "3")
                    val goalId = arguments?.getInt(EXTRA_GOAL_ID, 0)!!
                    personalGoalViewModel.initiatePersonalGoal(goalId = goalId)
                    personalGoalViewModel.fetchAndUpdateGoalProgress()
                    Log.d("PersonalGoal", "2")
                    // TODO: observe
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
                    personalGoalViewModel.stopUpdatingFetchingProgress()
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
                    try {
                        Log.d("RunGoalFragment Resume", "1: Setting Run Session Start Time")
                        runSessionViewModel.setRunSessionStartTime()

                        Log.d("RunGoalFragment Resume", "2: Fetching and Updating Stats")
                        runSessionViewModel.fetchAndUpdateStats()

                        Log.d("RunGoalFragment Resume", "3: Resuming Tracking")
                        resumeTracking()

                        Log.d("RunGoalFragment Resume", "4: Resuming GPS Tracking")
                        gpsTrackViewModel.resumeGPSTrack()
                        personalGoalViewModel.fetchAndUpdateGoalProgress()

                        Log.d("RunGoalFragment Resume", "All Resume Actions Completed")
                    } catch (e: Exception) {
                        Log.e("RunGoalFragment Resume", "Error occurred: ${e.message}", e)
                    }

                }
            }
        }

        binding.btnStop.setOnClickListener {
            it.findNavController().popBackStack(R.id.homeFragment, false)
            // TODO: STOP
            lifecycleScope.launch {
                mutex.withLock {
                    // TODO: stop gps tracking
                    gpsTrackViewModel.stopGPSTrack()
                    stopTracking()
                    personalGoalViewModel.stopUpdatingFetchingProgress()
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

    @SuppressLint("SetTextI18n")
    private fun initArrowAction() {
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
        lifecycleScope.launch {
            if (!isTracking) {
                isTracking = true
                // Clear previous route coordinates if restarting
                routeCoordinates.clear()

                // Set up the location listener for tracking
                val locationComponentPlugin = mapView.location
                locationComponentPlugin.updateSettings {
                    this.enabled = true // Enable location updates
                }
                indicatorListener = OnIndicatorPositionChangedListener { point ->
                    // TODO: Implement pause mechanism
                    routeCoordinates.add(point)

                    // TODO: Save <<point>> to database
                    Log.d("Longitude", point.longitude().toString())
                    Log.d("Latitude", point.latitude().toString())
                    // Draw the route
                    lifecycleScope.launch {
                        gpsPointViewModel.insertGPSPoint(point.longitude(), point.latitude())
                    }
                    drawRoute()
                }
                // Add the listener to start tracking
                indicatorListener?.let {
                    locationComponentPlugin.addOnIndicatorPositionChangedListener(
                        it
                    )
                }
            }
        }
    }

    private fun resumeTracking() {
        if (!isTracking) {
            isTracking = true

            // Set up the location listener for tracking
            val locationComponentPlugin = mapView.location
            locationComponentPlugin.updateSettings {
                this.enabled = true // Enable location updates
            }
            indicatorListener = OnIndicatorPositionChangedListener { point ->
                // TODO: Implement pause mechanism
                routeCoordinates.add(point)

                // TODO: Save <<point>> to database
                Log.d("Longitude", point.longitude().toString())
                Log.d("Latitude", point.latitude().toString())
                // Draw the route
                lifecycleScope.launch {
                    gpsPointViewModel.insertGPSPoint(point.longitude(), point.latitude())
                }
                drawRoute()
            }
            // Add the listener to start tracking
            indicatorListener?.let {
                locationComponentPlugin.addOnIndicatorPositionChangedListener(
                    it
                )
            }
        }
    }

    private fun stopTracking() {
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

    private fun pauseTracking() {
        if (isTracking) {
            isTracking = false

            // Remove the location listener
            val locationComponentPlugin = mapView.location

            indicatorListener?.let {
                locationComponentPlugin.removeOnIndicatorPositionChangedListener(it)
            }
            indicatorListener = null // Clear the reference

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

    override fun onStop() {
        super.onStop()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).isVisible = true
    }

    companion object {
        const val EXTRA_GOAL_ID = "EXTRA_GOAL_ID"
    }
}