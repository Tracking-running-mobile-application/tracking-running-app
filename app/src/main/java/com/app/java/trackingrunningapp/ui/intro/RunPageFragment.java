package com.app.java.trackingrunningapp.ui.intro;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import com.app.java.trackingrunningapp.R;
import com.app.java.trackingrunningapp.ui.FusedLocationAPI.DefaultLocationClient;
import com.app.java.trackingrunningapp.ui.FusedLocationAPI.LocationService;
import com.app.java.trackingrunningapp.ui.runPage.MetricItem;
import com.app.java.trackingrunningapp.ui.runPage.MetricItemAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class RunPageFragment extends Fragment {

    private DefaultLocationClient locationClient;
    private boolean isOverlayVisible = true;
    private boolean isTracking = false;

    public RunPageFragment() {
        super(R.layout.fragment_run_page);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = requireContext();
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        locationClient = new DefaultLocationClient(context, fusedLocationProviderClient);

        // Initialize the Start/Pause button
        Button toggleTrackingButton = view.findViewById(R.id.toggleTrackingButton);
        toggleTrackingButton.setText("Start Running Session");

        toggleTrackingButton.setOnClickListener(v -> {
            if (isTracking) {
                stopTracking();
                toggleTrackingButton.setText("Start Running Session");
            } else {
                // Start tracking only if permissions are granted
                if (locationClient.hasLocationPermission()) {
                    startTracking();
                    toggleTrackingButton.setText("Pause Running Session");
                } else {
                    // Request permissions if not already granted
                    locationClient.requestLocationPermissions(requireActivity());
                }
            }
            isTracking = !isTracking;
        });

        // Initialize overlay toggle button and RecyclerView
        ImageView toggleButton = view.findViewById(R.id.toggleButton);
        RecyclerView metricsRecyclerView = view.findViewById(R.id.metricsRecyclerView);
        metricsRecyclerView.setVisibility(View.VISIBLE);

        toggleButton.setOnClickListener(v -> {
            if (isOverlayVisible) {
                metricsRecyclerView.setVisibility(View.GONE);
                toggleButton.setRotation(180f);
            } else {
                metricsRecyclerView.setVisibility(View.VISIBLE);
                toggleButton.setRotation(0f);
            }
            isOverlayVisible = !isOverlayVisible;
        });

        // Set up metric items and RecyclerView adapter
        List<MetricItem> metricItems = new ArrayList<>();
        metricItems.add(new MetricItem("Duration", "01:25:40"));
        metricItems.add(new MetricItem("Distance", "10.02 km"));
        metricItems.add(new MetricItem("Pace", "9:12 / km"));
        metricItems.add(new MetricItem("Calories", "220 kcal"));

        metricsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        MetricItemAdapter adapter = new MetricItemAdapter(metricItems);
        metricsRecyclerView.setAdapter(adapter);

        // Request location permissions if not granted
        if (!locationClient.hasLocationPermission()) {
            locationClient.requestLocationPermissions(requireActivity());
        }
    }

    private void startTracking() {
        Context context = requireContext();
        Intent intent = new Intent(context, LocationService.class);
        intent.setAction(LocationService.ACTION_START);
        context.startService(intent);
    }

    private void stopTracking() {
        Context context = requireContext();
        Intent intent = new Intent(context, LocationService.class);
        intent.setAction(LocationService.ACTION_STOP);
        context.startService(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == DefaultLocationClient.LOCATION_PERMISSION_REQUEST_CODE) {
            if (locationClient.hasLocationPermission()) {
                startTracking();
            } else {
                Toast.makeText(requireContext(), "Location permission is required to track location", Toast.LENGTH_SHORT).show();
            }
        }
    }

}