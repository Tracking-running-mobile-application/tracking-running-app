package com.app.java.trackingrunningapp.ui.home.personalGoal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.model.dataclass.home.PersonalGoal


class PersonalGoalAdapter(
    private val taskList: List<PersonalGoal>,
    private val listener: OnItemPersonalGoalListener
) : RecyclerView.Adapter<PersonalGoalAdapter.PersonalGoalViewHolder>() {

    class PersonalGoalViewHolder(
        itemView: View,
        private val listener: OnItemPersonalGoalListener
    ) : RecyclerView.ViewHolder(itemView) {
        private val taskImage: ImageView = itemView.findViewById(R.id.taskImage)
        private val taskTitle: TextView = itemView.findViewById(R.id.text_task_title)
        private val taskCheckbox: ImageView = itemView.findViewById(R.id.taskCheckbox)
        private val icEditGoal = itemView.findViewById<ImageView>(R.id.ic_edit_goal)

        fun bind(dailyTask: PersonalGoal){
            taskTitle.text = dailyTask.title
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
            icEditGoal.setOnClickListener {
                it.findNavController().navigate(R.id.action_homeFragment_to_personalGoalFragment)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalGoalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_personal_goal, parent, false)
        return PersonalGoalViewHolder(view,listener)
    }

    override fun onBindViewHolder(holder: PersonalGoalViewHolder, position: Int) {
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
    interface OnItemPersonalGoalListener{
        fun onClick(dailyTask: PersonalGoal)
    }
}
