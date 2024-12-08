package com.app.java.trackingrunningapp.ui.personalGoal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.java.trackingrunningapp.R

class DayAdapter (private val dayList: List<DayItem>): RecyclerView.Adapter<DayAdapter.DayViewHolder>() {

    class DayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var dayName: TextView = view.findViewById(R.id.day_name)
        var dayTick: ImageView = view.findViewById(R.id.day_tick)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_day_selection, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val dayItem = dayList[position]
        holder.dayName.text = dayItem.name

        holder.dayTick.visibility = if (dayItem.isSelected) View.VISIBLE else View.INVISIBLE

        holder.itemView.setOnClickListener {
            dayItem.isSelected = !dayItem.isSelected
            notifyItemChanged(position)
        }

    }

    override fun getItemCount(): Int = dayList.size

}