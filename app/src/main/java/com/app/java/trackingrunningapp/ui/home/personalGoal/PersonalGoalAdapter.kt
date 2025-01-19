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
import com.app.java.trackingrunningapp.data.model.entity.User
import com.app.java.trackingrunningapp.data.repository.UserRepository
import com.app.java.trackingrunningapp.ui.viewmodel.UserViewModel


class PersonalGoalAdapter(
    private val personalGoals: MutableList<PersonalGoal>,
    private val user: User?,
    private val context: Context,
    private val listener: OnItemPersonalGoalListener,
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

        @SuppressLint("SetTextI18n")
        fun bind(goal: PersonalGoal, user: User?) {
            if(goal.goalProgress == 100.0){
                progress.text = "100%"
                taskCheckbox.visibility = View.VISIBLE
                icEditGoal.visibility = View.INVISIBLE
            }else{
                progress.text = context.getString(R.string.goal_progress,goal.goalProgress)
                taskCheckbox.visibility = View.INVISIBLE
                icEditGoal.visibility = View.VISIBLE
            }

            goalName.text = goal.name
            // target
            if (goal.targetDistance != 0.0 && goal.targetDistance != null) {
                if(user?.unit == "km"){
                    goalTarget.text = goal.targetDistance.toString() + " " + context.getString(R.string.km_lowercase)
                }else{
                    goalTarget.text = goal.targetDistance.toString() + " " + context.getString(R.string.mile_lowercase)
                }
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
                listener.onEditClick(goal)
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
        holder.bind(goal,user)
    }

    override fun getItemCount(): Int {
        return personalGoals.size
    }

    interface OnItemPersonalGoalListener {
        fun onClick(personalGoal: PersonalGoal)
        fun onEditClick(personalGoal: PersonalGoal)
    }
}
