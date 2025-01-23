package com.app.java.trackingrunningapp.ui.home.plan_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.model.dataclass.home.PlanExercise

class ListTrainingPlanAdapter(
    private val exercises: List<PlanExercise>,
    private val onItemExerciseClickListener: OnItemExerciseClickListener
) : RecyclerView.Adapter<ListTrainingPlanAdapter.ListTrainingViewHolder>() {

    class ListTrainingViewHolder(
        itemView: View,
        private val onItemExerciseClickListener: OnItemExerciseClickListener
    ) : RecyclerView.ViewHolder(itemView) {
        private val exerciseName = itemView.findViewById<TextView>(R.id.item_title_exercise)
        private val exerciseObjective =
            itemView.findViewById<TextView>(R.id.item_objective_exercise)
        private val exerciseImage = itemView.findViewById<ImageView>(R.id.item_exercise_image)

        fun bind(exercise: PlanExercise) {
            exerciseImage.setImageResource(exercise.imageRes)
            exerciseName.text = exercise.title
            exerciseObjective.text = itemView.context.getString(
                R.string.objective_amount,
                exercise.type,
                exercise.level
            )
            itemView.setOnClickListener {
                onItemExerciseClickListener.onExerciseClick(exercise)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListTrainingViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_plan_exercise, parent, false)
        return ListTrainingViewHolder(itemView, onItemExerciseClickListener)
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    override fun onBindViewHolder(holder: ListTrainingViewHolder, position: Int) {
        holder.bind(exercises[position])
    }

    interface OnItemExerciseClickListener {
        fun onExerciseClick(exercise:PlanExercise)
    }
}