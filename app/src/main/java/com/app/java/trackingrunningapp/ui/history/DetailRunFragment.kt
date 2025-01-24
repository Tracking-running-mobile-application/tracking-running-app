package com.app.java.trackingrunningapp.ui.history

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.model.entity.User
import com.app.java.trackingrunningapp.data.repository.RunSessionRepository
import com.app.java.trackingrunningapp.databinding.FragmentDetailRunBinding
import com.app.java.trackingrunningapp.ui.run.RunResultFragment.Companion.EXTRA_RUN_ID_RESULT
import com.app.java.trackingrunningapp.ui.viewmodel.GPSTrackViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.GPSTrackViewModelFactory
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModelFactory
import com.app.java.trackingrunningapp.ui.viewmodel.UserViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.UserViewModelFactory
import com.app.java.trackingrunningapp.utils.DateTimeUtils
import com.app.java.trackingrunningapp.utils.StatsUtils
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.annotation.AnnotationPlugin
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPolylineAnnotationManager
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DetailRunFragment : Fragment() {
    private lateinit var binding: FragmentDetailRunBinding
    private lateinit var runSessionViewModel: RunSessionViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var gpsTrackViewModel: GPSTrackViewModel
    private lateinit var mapView: MapView
    private val routeCoordinates = mutableListOf<Point>()
    private lateinit var annotationApi: AnnotationPlugin
    private lateinit var polylineAnnotationManager: PolylineAnnotationManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val gpsTrackFactory = GPSTrackViewModelFactory(InitDatabase.gpsTrackRepository)
        gpsTrackViewModel =
            ViewModelProvider(this, gpsTrackFactory).get(GPSTrackViewModel::class.java)
        binding = FragmentDetailRunBinding.inflate(inflater, container, false)
        val runFactory = RunSessionViewModelFactory(InitDatabase.runSessionRepository)
        runSessionViewModel =
            ViewModelProvider(this, runFactory).get(RunSessionViewModel::class.java)

        val userFactory = UserViewModelFactory(InitDatabase.userRepository)
        userViewModel = ViewModelProvider(this,userFactory)[UserViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val runSessionId = arguments?.getInt(EXTRA_HISTORY_RUN_ID, 0)
        runSessionViewModel.runSessions.observe(viewLifecycleOwner) { sessions ->
            for (session in sessions) {
                if (session.sessionId == runSessionId) {
                    binding.historyRunDetail.textDistanceMetric.text =
                        getString(R.string.text_distance_metric, session.distance)
                    binding.historyRunDetail.textDurationMetric.text =
                        StatsUtils.formatDuration(session.duration!!)
                    userViewModel.fetchUserInfo()
                    userViewModel.userLiveData.observe(viewLifecycleOwner){
                        binding.historyRunProfile.textHistoryProfileName.text = it?.name
                        if(it?.metricPreference == User.UNIT_MILE){
                            binding.historyRunDetail.textPaceMetric.text =
                                getString(R.string.text_speed_metric_mile,session.speed)
                        }else{
                            binding.historyRunDetail.textPaceMetric.text =
                                getString(R.string.text_speed_metric,session.speed)
                        }
                    }
                    binding.historyRunDetail.textCalorieMetric.text =
                        getString(R.string.text_calorie_metric,session.caloriesBurned)
                    binding.historyRunProfile.textHistoryDetailDate.text = DateTimeUtils.formatDateHistoryDetailFormat(session.runDate)
                }
            }
        }
        initMap()
    }

    private fun initMap() {
        mapView = binding.mapView
        annotationApi = mapView.annotations
        polylineAnnotationManager = annotationApi.createPolylineAnnotationManager()
        val runId = arguments?.getInt(EXTRA_HISTORY_RUN_ID, 0) ?: 0
        Log.d("RunID", runId.toString())
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val locations =  gpsTrackViewModel.fetchGPSPoints(runId)
                for (location in locations) {
                    routeCoordinates.add(Point.fromLngLat(location.longitude, location.latitude))
                }
            }
            drawRoute()
        }

        lateinit var targetPoint: Point
        mapView.mapboxMap.loadStyle(Style.STANDARD) {
            val locationComponentPlugin = mapView.location
            locationComponentPlugin.updateSettings {
                this.enabled = true
            }
            mapView.location.locationPuck = LocationPuck2D(
                topImage = null,
                bearingImage = null,
                shadowImage = null
            )
            locationComponentPlugin.addOnIndicatorPositionChangedListener(object :
                OnIndicatorPositionChangedListener {
                override fun onIndicatorPositionChanged(point: Point) {
                    targetPoint = point
                    if (routeCoordinates.isNotEmpty()) {
                        targetPoint = routeCoordinates.first()
                    }
                    mapView.mapboxMap.setCamera(
                        CameraOptions.Builder()
                            .center(targetPoint)
                            .zoom(15.0)
                            .build()
                    )

                    locationComponentPlugin.removeOnIndicatorPositionChangedListener(this)
                }
            })
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
        const val EXTRA_HISTORY_RUN_ID = "EXTRA_HISTORY_RUN_ID"
    }
}