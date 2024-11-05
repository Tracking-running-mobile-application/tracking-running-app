package com.app.java.trackingrunningapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.java.trackingrunningapp.R

data class DailyTask(
    val title: String,
    val duration: String,
    val frequency: String,
    val imageResId: Int,  // Resource ID for the task image
    val isChecked: Boolean  // Whether the task is completed
)

class DailyTasksAdapter(
    private val taskList: List<DailyTask>
) : RecyclerView.Adapter<DailyTasksAdapter.DailyTasksViewHolder>() {

    class DailyTasksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskImage: ImageView = itemView.findViewById(R.id.taskImage)
        val taskTitle: TextView = itemView.findViewById(R.id.taskTitle)
        val taskDuration: TextView = itemView.findViewById(R.id.taskDuration)
        val taskFrequency: TextView = itemView.findViewById(R.id.taskFrequency)
        val taskCheckbox: ImageView = itemView.findViewById(R.id.taskCheckbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyTasksViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.daily_task, parent, false)
        return DailyTasksViewHolder(view)
    }

    override fun onBindViewHolder(holder: DailyTasksViewHolder, position: Int) {
        val task = taskList[position]

        holder.taskTitle.text = task.title
        holder.taskDuration.text = task.duration
        holder.taskFrequency.text = task.frequency
        holder.taskImage.setImageResource(task.imageResId)

        // Set the checkbox based on `isChecked` value
        if (task.isChecked) {
            holder.taskCheckbox.setImageResource(R.drawable.ic_check_circle)  // Replace with actual checked icon resource
            holder.taskCheckbox.visibility = View.VISIBLE
        } else {
            holder.taskCheckbox.visibility = View.GONE  // Hide the checkbox if not checked
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}
