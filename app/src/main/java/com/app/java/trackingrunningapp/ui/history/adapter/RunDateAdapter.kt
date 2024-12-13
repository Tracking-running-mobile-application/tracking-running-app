package com.app.java.trackingrunningapp.ui.history.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.ui.history.RunDate

class RunDateAdapter(
    private val runDates: List<RunDate>
) : RecyclerView.Adapter<RunDateAdapter.RunDateViewHolder>() {

    inner class RunDateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateTextView: TextView = view.findViewById(R.id.tv_run_month)
        val runRecycler: RecyclerView = view.findViewById(R.id.rv_history_run)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunDateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_date, parent, false)
        return RunDateViewHolder(view)
    }

    override fun onBindViewHolder(holder: RunDateViewHolder, position: Int) {
        val runDate = runDates[position]
        holder.dateTextView.text = runDate.date
        holder.runRecycler.adapter = RunAdapter(runDate.runs)
    }

    override fun getItemCount(): Int = runDates.size
}
