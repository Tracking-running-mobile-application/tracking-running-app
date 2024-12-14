package com.app.java.trackingrunningapp.ui.history.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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
        private val icStar = itemView.findViewById<ImageButton>(R.id.ic_star)
        private val icStarSelected = itemView.findViewById<ImageButton>(R.id.ic_star_selected)

        fun bind(itemRun: Run) {
            runTime.text = itemRun.runTime // change to time
            runDistance.text = itemRun.distance
            icStar.setOnClickListener {
                icStar.visibility = View.GONE
                icStarSelected.visibility = View.VISIBLE
                listener.onAddFavouriteClick(FAVOURITE_ADD)
            }
            icStarSelected.setOnClickListener {
                icStar.visibility = View.VISIBLE
                icStarSelected.visibility = View.GONE
                listener.onAddFavouriteClick(FAVOURITE_REMOVE)
            }
            itemView.setOnClickListener {
                listener.onItemClick(itemRun)
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

    companion object{
        const val FAVOURITE_ADD = 1
        const val FAVOURITE_REMOVE = 2
    }
}
