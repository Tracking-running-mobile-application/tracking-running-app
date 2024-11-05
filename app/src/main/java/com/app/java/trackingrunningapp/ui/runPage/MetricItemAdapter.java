package com.app.java.trackingrunningapp.ui.runPage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.java.trackingrunningapp.R;

import java.util.List;


public class MetricItemAdapter extends RecyclerView.Adapter<MetricItemAdapter.MetricViewHolder> {

    private List<MetricItem> MetricItems;

    public MetricItemAdapter(List<MetricItem> MetricItems) {
        this.MetricItems = MetricItems;
    }

    @NonNull
    @Override
    public MetricViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_run_metric, parent, false);
        return new MetricViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MetricViewHolder holder, int position) {
        MetricItem item = MetricItems.get(position);
        holder.labelTextView.setText(item.getLabel());
        holder.valueTextView.setText(item.getValue());
    }

    @Override
    public int getItemCount() {
        return MetricItems.size();
    }

    public static class MetricViewHolder extends RecyclerView.ViewHolder {
        TextView labelTextView, valueTextView;

        public MetricViewHolder(View itemView) {
            super(itemView);
            labelTextView = itemView.findViewById(R.id.labelTextView);
            valueTextView = itemView.findViewById(R.id.valueTextView);
        }
    }
}