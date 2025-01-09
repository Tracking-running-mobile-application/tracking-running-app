package com.app.java.trackingrunningapp.ui.profile.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.java.trackingrunningapp.R

data class RunItem(val runTime: String, val distance: String, val date: String)

class FavouriteRunAdapter(private val runList: MutableList<RunItem>) : RecyclerView.Adapter<FavouriteRunAdapter.RunViewHolder>() {
    inner class RunViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val runTime: TextView = itemView.findViewById(R.id.titleText)
        val distanceText: TextView = itemView.findViewById(R.id.distanceText)
        val dateText: TextView = itemView.findViewById(R.id.dateText)
        val starIcon: ImageView = itemView.findViewById(R.id.starIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fav_run, parent, false)
        return RunViewHolder(view)
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val runItem = runList[position]
        holder.runTime.text = runItem.runTime
        holder.distanceText.text = runItem.distance
        holder.dateText.text = runItem.date

        holder.starIcon.setOnClickListener {
            if (position != RecyclerView.NO_POSITION) {
                runList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, runList.size)
            }
        }

    }

    override fun getItemCount() = runList.size
}
