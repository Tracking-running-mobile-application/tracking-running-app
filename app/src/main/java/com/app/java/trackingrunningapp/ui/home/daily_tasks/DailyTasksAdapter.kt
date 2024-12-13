package com.app.java.trackingrunningapp.ui.home.daily_tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.model.DailyTask


class DailyTasksAdapter(
    private val taskList: List<DailyTask>,
    private val listener: OnItemDailyTaskListener
) : RecyclerView.Adapter<DailyTasksAdapter.DailyTasksViewHolder>() {

    class DailyTasksViewHolder(
        itemView: View,
        private val listener: OnItemDailyTaskListener
    ) : RecyclerView.ViewHolder(itemView) {
        private val taskImage: ImageView = itemView.findViewById(R.id.taskImage)
        private val taskTitle: TextView = itemView.findViewById(R.id.taskTitle)
        private val taskDuration: TextView = itemView.findViewById(R.id.taskDuration)
        private val taskFrequency: TextView = itemView.findViewById(R.id.taskFrequency)
        private val taskCheckbox: ImageView = itemView.findViewById(R.id.taskCheckbox)

        fun bind(dailyTask: DailyTask){
            taskTitle.text = dailyTask.title
            taskFrequency.text = dailyTask.frequency
            taskDuration.text = dailyTask.duration
            taskImage.setImageResource(dailyTask.imageResId)
            // TODO: handle check box
            itemView.setOnClickListener {
                listener.onClick(dailyTask)
                if (dailyTask.isClicked % 2 == 0){
                    taskCheckbox.setImageResource(R.drawable.ic_check_circle)
                    taskCheckbox.visibility = View.VISIBLE
                }else{
                    taskCheckbox.visibility = View.INVISIBLE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyTasksViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_daily_task, parent, false)
        return DailyTasksViewHolder(view,listener)
    }

    override fun onBindViewHolder(holder: DailyTasksViewHolder, position: Int) {
        val task = taskList[position]
        holder.bind(task)
        // Set the checkbox based on `isChecked` value
//        if (task.isChecked) {
//            holder.taskCheckbox.setImageResource(R.drawable.ic_check_circle)  // Replace with actual checked icon resource
//            holder.taskCheckbox.visibility = View.VISIBLE
//        } else {
//            holder.taskCheckbox.visibility = View.GONE  // Hide the checkbox if not checked
//        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
    interface OnItemDailyTaskListener{
        fun onClick(dailyTask: DailyTask)
    }
}
