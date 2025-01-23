package com.app.java.trackingrunningapp.ui.profile.favorite

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.model.entity.RunSession
import com.app.java.trackingrunningapp.utils.DateTimeUtils
import com.app.java.trackingrunningapp.utils.StatsUtils


class FavouriteRunAdapter(
    private val runList: MutableList<RunSession?>,
    private val context: Context,
    private val listener: OnStarClickListener
) : RecyclerView.Adapter<FavouriteRunAdapter.RunViewHolder>() {
    inner class RunViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val runTime: TextView = itemView.findViewById(R.id.titleText)
        private val runDistance: TextView = itemView.findViewById(R.id.distanceText)
        private val runDate: TextView = itemView.findViewById(R.id.dateText)
        val starIcon: ImageView = itemView.findViewById(R.id.starIcon)

        fun bind(itemRun: RunSession?) {
            runTime.text = StatsUtils.formatDuration(itemRun?.duration ?: 0L)
            runDistance.text = context.getString(R.string.text_distance_metric, itemRun?.distance)
            runDate.text = DateTimeUtils.formatDateString(itemRun?.runDate!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        val view = LayoutInflater.from(parent.context).
        inflate(R.layout.item_fav_run, parent, false)
        return RunViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val runItem = runList[position]
        holder.bind(runItem)
        holder.starIcon.setOnClickListener {
            listener.onDeleteFavourite(runItem)
            runList.remove(runItem)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount() = runList.size

    interface OnStarClickListener {
        fun onDeleteFavourite(itemRun: RunSession?)
    }
}
