package com.app.java.trackingrunningapp.ui.history.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.model.dataclass.history.RunDate
import com.app.java.trackingrunningapp.data.model.entity.RunSession
import com.app.java.trackingrunningapp.ui.history.OnItemHistoryRunClickListener

class RunDateAdapter(
    private val runDates: MutableList<RunDate> = mutableListOf(),
    private val itemRunListener: OnItemHistoryRunClickListener
) : RecyclerView.Adapter<RunDateAdapter.RunDateViewHolder>() {
    inner class RunDateViewHolder(
        view: View,
        private val listener: OnItemHistoryRunClickListener
    ) : RecyclerView.ViewHolder(view) {
        private val dateTextView: TextView = view.findViewById(R.id.tv_run_month)
        private val runRecycler: RecyclerView = view.findViewById(R.id.rv_history_run)

        fun bind(runDate: RunDate) {
            dateTextView.text = runDate.date
            // set up run adapter
            val runAdapter = RunAdapter(runDate.runs,listener)
            runAdapter.updateRunHistory(runDate.runs)
            runRecycler.adapter = runAdapter
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateRunDate(listRunDate:List<RunDate>){
        runDates.clear()
        runDates.addAll(listRunDate)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunDateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_date, parent, false)
        return RunDateViewHolder(view,itemRunListener)
    }

    override fun onBindViewHolder(holder: RunDateViewHolder, position: Int) {
        val runDate = runDates[position]
        holder.bind(runDate)
    }

    override fun getItemCount(): Int {
        return runDates.size
    }
}
