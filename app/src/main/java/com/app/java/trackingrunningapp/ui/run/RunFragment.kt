package com.app.java.trackingrunningapp.ui.run

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.app.java.trackingrunningapp.databinding.FragmentRunBinding
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.locationcomponent.location

class RunFragment : Fragment() {
    private lateinit var binding: FragmentRunBinding
    private lateinit var mapView: MapView
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
        binding = FragmentRunBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("InlinedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArrowAction()
        mapView = binding.mapView

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
        /*  mapView.mapboxMap.loadStyle(Style.STANDARD) { style ->
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

         */
        initMap()

    }

    private fun initMap() {
//        mapView = MapView(requireContext())
        mapView.mapboxMap.loadStyle(Style.MAPBOX_STREETS) {
//            setCamera(
//                CameraOptions.Builder()
//                    .center(Point.fromLngLat(21.0481, 105.80125))
//                    .pitch(45.0)
//                    .zoom(15.0)
//                    .build()
//            )
            val locationPlugin = mapView.location
            locationPlugin.updateSettings {
                enabled = true
            }
            locationPlugin.addOnIndicatorPositionChangedListener { point ->
                moveCameraToLocation(point)
            }
        }
    }

    private fun moveCameraToLocation(location: Point) {
        mapView.mapboxMap.setCamera(
            CameraOptions.Builder()
                .center(location)
                .zoom(14.0)
                .build()
        )
    }


    override fun onStop() {
        super.onStop()
        binding.icArrowDown.visibility = View.GONE
    }
}