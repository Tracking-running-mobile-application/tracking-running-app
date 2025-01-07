package com.app.java.trackingrunningapp.ui.home.personalGoal

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.model.entity.PersonalGoal


class PersonalGoalAdapter(
    private val personalGoals: MutableList<PersonalGoal>,
    private val context: Context,
    private val listener: OnItemPersonalGoalListener
) : RecyclerView.Adapter<PersonalGoalAdapter.PersonalGoalViewHolder>() {
    val listGoal = personalGoals
    class PersonalGoalViewHolder(
        itemView: View,
        private val context: Context,
        private val listener: OnItemPersonalGoalListener
    ) : RecyclerView.ViewHolder(itemView) {
        private val progress: TextView = itemView.findViewById(R.id.text_percent)
        private val goalName: TextView = itemView.findViewById(R.id.text_goal_name)
        private val goalTarget: TextView = itemView.findViewById(R.id.text_goal_target)
        private val taskCheckbox: ImageView = itemView.findViewById(R.id.taskCheckbox)
        private val icEditGoal = itemView.findViewById<ImageView>(R.id.ic_edit_goal)

        fun bind(goal: PersonalGoal) {
            progress.text = context.getString(R.string.goal_progress,goal.goalProgress)
            goalName.text = goal.name
            // target
            if (goal.targetDistance != 0.0 && goal.targetDistance != null) {
                goalTarget.text =
                    context.getString(R.string.text_target_distance, goal.targetDistance)
            } else if (goal.targetDuration != 0.0 && goal.targetDuration != null) {
                goalTarget.text =
                    context.getString(R.string.text_estimate_time, goal.targetDuration)
            } else if (goal.targetCaloriesBurned != 0.0 && goal.targetCaloriesBurned != null) {
                goalTarget.text =
                    context.getString(R.string.text_calorie_metric, goal.targetCaloriesBurned)
            }
            // TODO: handle check box
            itemView.setOnClickListener {
                listener.onClick(goal)
            }
            icEditGoal.setOnClickListener {
                it.findNavController()
                    .navigate(R.id.action_homeFragment_to_personalGoalFragment)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updatePersonalGoal(goals:List<PersonalGoal>){
        personalGoals.clear()
        personalGoals.addAll(goals)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalGoalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_personal_goal, parent, false)
        return PersonalGoalViewHolder(view, context, listener)
    }

    override fun onBindViewHolder(holder: PersonalGoalViewHolder, position: Int) {
        val goal = personalGoals[position]
        holder.bind(goal)
    }

    override fun getItemCount(): Int {
        return personalGoals.size
    }

    interface OnItemPersonalGoalListener {
        fun onClick(personalGoal: PersonalGoal)
    }
}
