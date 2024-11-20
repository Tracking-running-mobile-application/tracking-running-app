package com.app.java.trackingrunningapp.ui.run

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.databinding.FragmentRunBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class RunFragment : Fragment(),OnMapReadyCallback {
    private lateinit var binding: FragmentRunBinding
    private var mGoogleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRunBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val supportMapFragment = childFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment?
//        mapFragment.getMapAsync(this)
        supportMapFragment?.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {
        mGoogleMap = p0
        val sydney = LatLng(21.0481,105.8012)
        val markerOptions = MarkerOptions().position(sydney).title("Marked in Sydney")
        mGoogleMap?.addMarker(markerOptions)
        mGoogleMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN
        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15f))
        mGoogleMap?.moveCamera(CameraUpdateFactory.zoomTo(15f))
        mGoogleMap?.uiSettings?.isZoomControlsEnabled = true
        mGoogleMap?.uiSettings?.isCompassEnabled = true
        mGoogleMap?.uiSettings?.isZoomGesturesEnabled = true

        val polylineOptions = PolylineOptions()
            .add( LatLng(21.0481, 105.8012),
                LatLng(21.0450, 105.7922))

        val polyline = mGoogleMap?.addPolyline(polylineOptions)

    }
}