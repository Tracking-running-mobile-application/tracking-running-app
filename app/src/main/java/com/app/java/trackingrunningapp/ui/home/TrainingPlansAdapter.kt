package com.app.java.trackingrunningapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.java.trackingrunningapp.R

data class TrainingPlan(val name: String, val imageResId: Int)

class TrainingPlansAdapter(private val itemList: List<TrainingPlan>) : RecyclerView.Adapter<TrainingPlansAdapter.TrainingPlansViewHolder>() {

    class TrainingPlansViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val planName: TextView = itemView.findViewById(R.id.planName)
        val planImage: ImageView = itemView.findViewById(R.id.planImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingPlansViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.training_plan, parent, false)
        return TrainingPlansViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrainingPlansViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.planName.text = currentItem.name
        holder.planImage.setImageResource(currentItem.imageResId) // Make sure this matches the ImageView ID
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
