package com.app.java.trackingrunningapp.ui.history.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.model.dataclass.history.Run
import com.app.java.trackingrunningapp.data.model.entity.RunSession
import com.app.java.trackingrunningapp.ui.history.OnItemHistoryRunClickListener
import com.app.java.trackingrunningapp.utils.DateTimeUtils
import com.app.java.trackingrunningapp.utils.StatsUtils

class RunAdapter(
    private val runs: List<RunSession>,
    private val context:Context,
    private val listener: OnItemHistoryRunClickListener
) : RecyclerView.Adapter<RunAdapter.RunViewHolder>() {
    inner class RunViewHolder(
        itemView: View,
        private val context:Context,
        private val listener: OnItemHistoryRunClickListener
    ) : RecyclerView.ViewHolder(itemView) {
        private val runTime: TextView = itemView.findViewById(R.id.runTime)
        private val runDistance: TextView = itemView.findViewById(R.id.runDistance)
        private val runDate:TextView = itemView.findViewById(R.id.text_rundate_history)
        private val icStar = itemView.findViewById<ImageButton>(R.id.ic_star)
        private val icStarSelected = itemView.findViewById<ImageButton>(R.id.ic_star_selected)

        @SuppressLint("SetTextI18n")
        fun bind(itemRun: RunSession) {
            runTime.text = StatsUtils.formatDuration(itemRun.duration ?: 0L)
            runDistance.text = context.getString(R.string.text_distance_metric,itemRun.distance)
            runDate.text = DateTimeUtils.formatDateString(itemRun.runDate)
            if(itemRun.isFavorite){
                icStarSelected.visibility = View.VISIBLE
            }
            icStar.setOnClickListener {
                icStar.visibility = View.INVISIBLE
                icStarSelected.visibility = View.VISIBLE
                listener.onAddFavouriteClick(FAVOURITE_ADD,itemRun)
            }
            icStarSelected.setOnClickListener {
                icStar.visibility = View.VISIBLE
                icStarSelected.visibility = View.GONE
                listener.onAddFavouriteClick(FAVOURITE_REMOVE,itemRun)
            }
            itemView.setOnClickListener {
                listener.onItemClick(itemRun)
            }
        }
    }

//    @SuppressLint("NotifyDataSetChanged")
//    fun updateRunHistory(listRun: MutableList<Run>) {
//        runs.clear()
//        runs.addAll(listRun)
//        notifyDataSetChanged()
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_run, parent, false)
        return RunViewHolder(view, context,listener)
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val run = runs[position]
        holder.bind(run)
    }

    override fun getItemCount(): Int = runs.size

    companion object {
        const val FAVOURITE_ADD = 1
        const val FAVOURITE_REMOVE = 2
    }
}
