package com.app.java.trackingrunningapp.ui.home.training

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
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.model.entity.User
import com.app.java.trackingrunningapp.data.repository.UserRepository
import com.app.java.trackingrunningapp.databinding.FragmentRunPlanBinding
import com.app.java.trackingrunningapp.ui.viewmodel.GPSPointViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.GPSPointViewModelFactory
import com.app.java.trackingrunningapp.ui.viewmodel.GPSTrackViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.GPSTrackViewModelFactory
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModelFactory
import com.app.java.trackingrunningapp.ui.viewmodel.TrainingPlanViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.TrainingPlanViewModelFactory
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


class RunPlanFragment : Fragment() {
    private lateinit var binding: FragmentRunPlanBinding
    private var isTracking: Boolean = false
    private lateinit var mapView: MapView
    private val routeCoordinates = mutableListOf<Point>()
    private lateinit var annotationApi: AnnotationPlugin
    private lateinit var polylineAnnotationManager: PolylineAnnotationManager
    private lateinit var runSessionViewModel: RunSessionViewModel
    private lateinit var trainingPlanViewModel: TrainingPlanViewModel
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

        val planFactory = TrainingPlanViewModelFactory(
            InitDatabase.trainingPlanRepository,
            InitDatabase.notificationRepository,
            InitDatabase.runSessionRepository
        )
        trainingPlanViewModel =
            ViewModelProvider(this, planFactory).get(TrainingPlanViewModel::class.java)

        val gpsTrackFactory = GPSTrackViewModelFactory(InitDatabase.gpsTrackRepository)
        gpsTrackViewModel =
            ViewModelProvider(this, gpsTrackFactory).get(GPSTrackViewModel::class.java)

        val gpsPointFactory = GPSPointViewModelFactory(InitDatabase.gpsPointRepository)
        gpsPointViewModel =
            ViewModelProvider(this, gpsPointFactory).get(GPSPointViewModel::class.java)

        val userFactory = UserViewModelFactory(InitDatabase.userRepository)
        userViewModel = ViewModelProvider(this,userFactory)[UserViewModel::class.java]

        binding = FragmentRunPlanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPermission()
        setupActionRun()
        initArrowAction()
        setupProgress()
    }

    @SuppressLint("SetTextI18n")
    private fun setupProgress() {
        // PLAN ID
        val planId = arguments?.getInt(EXTRA_PLAN_ID,0)
        Log.d("plan_Idd","$planId")
        trainingPlanViewModel.fetchRecommendedPlans()
        trainingPlanViewModel.recommendedPlansBeginner.observe(viewLifecycleOwner){plans->
            for(plan in plans){
                if(plan.planId == planId){
                    binding.progressBar.progress = plan.goalProgress?.toInt() ?: 0
                    binding.textRunPercent.text = getString(R.string.text_goal_progress,plan.goalProgress)
                }
            }
        }
        trainingPlanViewModel.recommendedPlansIntermediate.observe(viewLifecycleOwner){plans->
            for(plan in plans){
                if(plan.planId == planId){
                    binding.progressBar.progress = plan.goalProgress?.toInt() ?: 0
                    binding.textRunPercent.text =  getString(R.string.text_goal_progress,plan.goalProgress)
                }
            }
        }
        trainingPlanViewModel.recommendedPlansAdvanced.observe(viewLifecycleOwner){plans->
            for(plan in plans){
                if(plan.planId == planId){
                    binding.progressBar.progress = plan.goalProgress?.toInt() ?: 0
                    binding.textRunPercent.text =  getString(R.string.text_goal_progress,plan.goalProgress)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
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
                    Log.d("Run Plan Fragment", "1")
                    gpsTrackViewModel.initiateGPSTrack()
                    Log.d("Run Plan Fragment", "2")
                    runSessionViewModel.setRunSessionStartTime()
                    Log.d("Run Plan Fragment", "3")
                    // TODO: insert start tracking and sending gps function
                    startTracking()
                    Log.d("Run Plan Fragment", "4")
                    runSessionViewModel.fetchAndUpdateStats()
                    Log.d("Run Plan Fragment", "5")
                    val planId = arguments?.getInt(EXTRA_PLAN_ID, 0)!!
                    trainingPlanViewModel.initiateTrainingPlan(planId = planId)
                    Log.d("Run Plan Fragment", "6")
                    trainingPlanViewModel.fetchAndUpdateGoalProgress()
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
                    Log.d("RunPlanFragment Pause", "1")
                    runSessionViewModel.pauseRunSession()
                    trainingPlanViewModel.stopUpdatingFetchingProgress()
                    Log.d("RunPlanFragment Pause", "2")
                    pauseTracking()
                    Log.d("RunPlanFragment Pause", "3")
                    gpsTrackViewModel.stopGPSTrack()
                    Log.d("RunPlanFragment Pause", "4")
                }
            }
        }

        binding.btnResume.setOnClickListener {
            binding.btnResume.visibility = View.INVISIBLE
            binding.btnPause.visibility = View.VISIBLE
            // TODO: RESUME
            lifecycleScope.launch {
                try {
                    runSessionViewModel.setRunSessionStartTime()
                    trainingPlanViewModel.fetchAndUpdateGoalProgress()
                    resumeTracking()
                    gpsTrackViewModel.resumeGPSTrack()
                    runSessionViewModel.fetchAndUpdateStats()
                } catch (e: Exception) {
                    Log.e("RunPlanFragment Resume", "Error in stopTracking: ${e.message}")
                }
                Log.d("RunPlanFragment Resume", "Stop Sequence Complete")
            }
        }

        binding.btnStop.setOnClickListener {
            // TODO: STOP
            it.findNavController().popBackStack(R.id.trainingPlanFragment, false)
            lifecycleScope.launch {

                mutex.withLock {
                    // TODO: stop gps tracking
                    try {
                        Log.d("RunPlanFragment Stop", "1: Stopping GPS Tracking")
                        gpsTrackViewModel.stopGPSTrack()

                        Log.d("RunPlanFragment Stop", "2: Stopping Tracking")
                        stopTracking()
                        trainingPlanViewModel.stopUpdatingFetchingProgress()
                        Log.d("RunPlanFragment Stop", "3: Finishing Run Session")
                        runSessionViewModel.finishRunSession()

                        Log.d("RunPlanFragment Stop", "4: Completed All Stop Actions")
                    } catch (e: Exception) {
                        Log.e("RunPlanFragment Stop", "Error occurred: ${e.message}", e)
                    }
                    try {
                        mutex.withLock {
                            Log.d("RunPlanFragment Stop", "Starting Stop Sequence")
                            try {
                                Log.d("RunPlanFragment Stop", "2 - stopGPSTrack")
                                gpsTrackViewModel.stopGPSTrack()
                            } catch (e: Exception) {
                                Log.e("RunPlanFragment Stop", "Error in stopGPSTrack: ${e.message}")
                            }

                            try {
                                Log.d("RunPlanFragment Stop", "3 - stopTracking")
                                stopTracking()
                            } catch (e: Exception) {
                                Log.e("RunPlanFragment Stop", "Error in stopTracking: ${e.message}")
                            }

                            try {
                                Log.d("RunPlanFragment Stop", "4 - finishRunSession")
                                runSessionViewModel.finishRunSession()
                            } catch (e: Exception) {
                                Log.e(
                                    "RunPlanFragment Stop",
                                    "Error in finishRunSession: ${e.message}"
                                )
                            }
                            Log.d("RunPlanFragment Stop", "Stop Sequence Complete")
                        }
                    } catch (e: Exception) {
                        Log.e("RunPlanFragment Stop", "Error in lifecycleScope: ${e.message}")

                    }
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

        userViewModel.fetchUserInfo()
        userViewModel.userLiveData.observe(viewLifecycleOwner) { user ->
            runSessionViewModel.statsFlow.observe(viewLifecycleOwner) {
                runDuration.text = getString(R.string.text_duration_metric, it?.duration ?: 0.0)
                if(user?.metricPreference == User.UNIT_MILE){
                    runPace.text = getString(R.string.text_speed_metric_mile, it?.pace ?: 0.0)
                }else{
                    runPace.text = getString(R.string.text_speed_metric, it?.pace ?: 0.0)
                }
                runCalo.text = getString(R.string.text_calorie_metric, it?.caloriesBurned ?: 0.0)
                if (user?.metricPreference == User.UNIT_KM) {
                    runDistance.text = getString(R.string.text_distance_metric, it?.distance ?: 0.0)
                } else if (user?.metricPreference == User.UNIT_MILE) {
                    runDistance.text =
                        getString(R.string.text_distance_metric_mile, it?.distance ?: 0.0)
                }
            }
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

    companion object {
        const val EXTRA_PLAN_ID = "EXTRA_PLAN_ID"
    }
}