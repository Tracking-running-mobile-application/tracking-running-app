package com.app.java.trackingrunningapp.ui.history.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.ui.history.Run

class RunAdapter(private val runs: List<Run>) : RecyclerView.Adapter<RunAdapter.RunViewHolder>() {

    inner class RunViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val runType: TextView = itemView.findViewById(R.id.runType)
        val runDistance: TextView = itemView.findViewById(R.id.runDistance)
        val runDate: TextView = itemView.findViewById(R.id.runDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_run, parent, false)
        return RunViewHolder(view)
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val run = runs[position]
        holder.runType.text = run.type
        holder.runDistance.text = run.distance
        holder.runDate.text = run.date
    }

    override fun getItemCount(): Int = runs.size
}
