package com.app.java.trackingrunningapp.ui.newfavourite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.java.trackingrunningapp.R

data class RunItem(val title: String, val distance: String, val date: String)

class FavouriteRunAdapter(private val runList: MutableList<RunItem>) : RecyclerView.Adapter<FavouriteRunAdapter.RunViewHolder>() {
    inner class RunViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.titleText)
        val distanceText: TextView = itemView.findViewById(R.id.distanceText)
        val dateText: TextView = itemView.findViewById(R.id.dateText)
        val starIcon: ImageView = itemView.findViewById(R.id.starIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fav_run_item, parent, false)
        return RunViewHolder(view)
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val runItem = runList[position]
        holder.titleText.text = runItem.title
        holder.distanceText.text = runItem.distance
        holder.dateText.text = runItem.date

        holder.starIcon.setOnClickListener{
            runList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount() = runList.size
}
