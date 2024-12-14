package com.app.java.trackingrunningapp.ui.history.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.model.history.Run
import com.app.java.trackingrunningapp.ui.history.OnItemHistoryRunClickListener

class RunAdapter(
    private val listener: OnItemHistoryRunClickListener
) : RecyclerView.Adapter<RunAdapter.RunViewHolder>() {
    private val runs = mutableListOf<Run>()
    inner class RunViewHolder(
        itemView: View,
        private val listener: OnItemHistoryRunClickListener
    ) : RecyclerView.ViewHolder(itemView) {
        private val runTime: TextView = itemView.findViewById(R.id.runTime)
        private val runDistance: TextView = itemView.findViewById(R.id.runDistance)

        fun bind(itemRun: Run) {
            runTime.text = itemRun.runTime // change to time
            runDistance.text = itemRun.distance
            itemView.setOnClickListener {
                listener.onClick(itemRun)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateRunHistory(listRun: List<Run>){
        runs.clear()
        runs.addAll(listRun)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_run, parent, false)
        return RunViewHolder(view,listener)
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val run = runs[position]
        holder.bind(run)
    }

    override fun getItemCount(): Int = runs.size
}
