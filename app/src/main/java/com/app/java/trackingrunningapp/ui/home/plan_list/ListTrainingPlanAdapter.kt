package com.app.java.trackingrunningapp.ui.home.plan_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.model.entity.TrainingPlan

class ListTrainingPlanAdapter(
    private val listExercise: List<TrainingPlan>
) : RecyclerView.Adapter<ListTrainingPlanAdapter.ListTrainingViewHolder>() {

    class ListTrainingViewHolder(
        private val itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListTrainingViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_plan_exercise,parent,false)
        return ListTrainingViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listExercise.size
    }

    override fun onBindViewHolder(holder: ListTrainingViewHolder, position: Int) {

    }
}