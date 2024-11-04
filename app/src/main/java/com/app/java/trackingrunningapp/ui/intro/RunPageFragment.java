package com.app.java.trackingrunningapp.ui.intro;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import com.app.java.trackingrunningapp.R;
import com.app.java.trackingrunningapp.ui.runPage.MetricItem;
import com.app.java.trackingrunningapp.ui.runPage.MetricItemAdapter;

public class RunPageFragment extends Fragment {
    private boolean isOverlayVisible = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_run_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<MetricItem> metricItems = new ArrayList<>();
        metricItems.add(new MetricItem("Duration", "01:25:40"));
        metricItems.add(new MetricItem("Distance", "10.02 km"));
        metricItems.add(new MetricItem("Pace", "9:12 / km"));
        metricItems.add(new MetricItem("Calories", "220 kcal"));

        RecyclerView metricsRecyclerView = view.findViewById(R.id.metricsRecyclerView);
        metricsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        MetricItemAdapter adapter = new MetricItemAdapter(metricItems);
        metricsRecyclerView.setAdapter(adapter);

        ImageView toggleButton = view.findViewById(R.id.toggleButton);
        metricsRecyclerView.setVisibility(View.VISIBLE);

        toggleButton.setOnClickListener(v -> {
            if (isOverlayVisible) {
                metricsRecyclerView.setVisibility(View.GONE);
                toggleButton.setRotation(180);
            } else {
                metricsRecyclerView.setVisibility(View.VISIBLE);
                toggleButton.setRotation(0);
            }
            isOverlayVisible = !isOverlayVisible;
        });
    }
}