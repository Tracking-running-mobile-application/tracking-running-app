package com.app.java.trackingrunningapp.ui.run_page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.ui.run_page.MetricItemAdapter.MetricViewHolder

class MetricItemAdapter(private val MetricItems: List<MetricItem>) :
    RecyclerView.Adapter<MetricViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MetricViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_run_metric, parent, false)
        return MetricViewHolder(view)
    }

    override fun onBindViewHolder(holder: MetricViewHolder, position: Int) {
        val item = MetricItems[position]
        holder.labelTextView.text = item.label
        holder.valueTextView.text = item.value
    }

    override fun getItemCount(): Int {
        return MetricItems.size
    }

    class MetricViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var labelTextView: TextView = itemView.findViewById(R.id.labelTextView)
        var valueTextView: TextView = itemView.findViewById(R.id.valueTextView)
    }
}