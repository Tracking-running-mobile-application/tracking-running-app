package com.app.java.trackingrunningapp.ui.run

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.model.dataclass.location.Location
import com.app.java.trackingrunningapp.databinding.FragmentRunResultBinding
import com.app.java.trackingrunningapp.ui.viewmodel.GPSTrackViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.GPSTrackViewModelFactory
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RunResultFragment : Fragment() {
    private lateinit var binding:FragmentRunResultBinding
    private lateinit var mapView: MapView
    private val routeCoordinates = mutableListOf<Point>()
    private lateinit var annotationApi: AnnotationPlugin
    private lateinit var polylineAnnotationManager: PolylineAnnotationManager
    private lateinit var gpsTrackViewModel: GPSTrackViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val gpsTrackFactory = GPSTrackViewModelFactory(InitDatabase.gpsTrackRepository)
        gpsTrackViewModel =
            ViewModelProvider(this, gpsTrackFactory).get(GPSTrackViewModel::class.java)
        binding = FragmentRunResultBinding.inflate(inflater,container,false)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()
        setUpToolbar()
        setUpAction()
    }

    private fun setUpAction() {
        binding.btnRunSave.setOnClickListener {
            it.findNavController().popBackStack(R.id.action_global_homeFragment,false)
            Toast.makeText(requireContext(),"Added Successful",Toast.LENGTH_SHORT).show()
        }
        binding.btnDiscard.setOnClickListener {
            it.findNavController().navigate(R.id.action_global_runFragment)
            Toast.makeText(requireContext(),"Added Failure",Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpToolbar() {
        val toolbarTitle = requireActivity().findViewById<TextView>(R.id.tv_toolbar_title)
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar_main)
        toolbar.navigationIcon = null
        toolbarTitle.text = "Run Result"
    }

    override fun onStop() {
        super.onStop()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).visibility = View.VISIBLE
    }

    private fun initMap() {
        mapView = binding.mapView
        annotationApi = mapView.annotations
        polylineAnnotationManager = annotationApi.createPolylineAnnotationManager()
//        TODO: Load points of this session with proper sessionId
        lifecycleScope.launch {
             withContext(Dispatchers.IO) {
                 val locations =  gpsTrackViewModel.fetchGPSPoints(77)
                 for (location in locations) {
                     routeCoordinates.add(Point.fromLngLat(location.longitude, location.latitude))
                 }
            }
            drawRoute()
        }

        mapView.mapboxMap.loadStyle(Style.STANDARD) {
            val targetPoint = routeCoordinates.last()
            mapView.mapboxMap.setCamera(
                CameraOptions.Builder()
                    .center(targetPoint)
                    .zoom(15.0)
                    .build()
            )
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