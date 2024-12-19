package com.app.java.trackingrunningapp.ui.home

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.model.dataclass.home.TrainingPlan
import java.io.File


class HomeTrainingPlanAdapter(
    private val trainingPlans: MutableList<TrainingPlan>,
    private val listener: OnItemTrainingClickListener
) : RecyclerView.Adapter<HomeTrainingPlanAdapter.TrainingPlansViewHolder>() {

    class TrainingPlansViewHolder(
        itemView: View,
        private val listener: OnItemTrainingClickListener
    ) : RecyclerView.ViewHolder(itemView) {
        private val planName: TextView = itemView.findViewById(R.id.item_plan_name)
        private val planImage: ImageView = itemView.findViewById(R.id.item_plan_image)

        fun bind(trainingPlan: TrainingPlan) {
            planName.text = trainingPlan.name
//            planImage.setImageResource(trainingPlan.imageResId) // Make sure this matches the ImageView ID
            if (trainingPlan.imageResId != null) {
                val imgFile = File("tracking-running-app/app/src/main/res/drawable/img04.jpg")
                if (imgFile.exists()) {
                    val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                    if (myBitmap != null) {
                        planImage.setImageBitmap(myBitmap)
                    } else {
                        Log.e("ImageError", "Failed to decode image from file: $imgFile")
                    }
                } else {
                    Log.e("FileError", "Image file does not exist: $imgFile")
                }
            } else {
                Log.e("ImageResIdError", "ImageResId is null.")
            }
//            planImage.setImageResource(R.drawable.img04)

            itemView.setOnClickListener {
                listener.onClick(trainingPlan)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingPlansViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_training_plan, parent, false)
        return TrainingPlansViewHolder(view,listener)
    }

    override fun onBindViewHolder(holder: TrainingPlansViewHolder, position: Int) {
        val trainingPlan = trainingPlans[position]
        holder.bind(trainingPlan)
    }

    override fun getItemCount(): Int {
        return trainingPlans.size
    }

    interface OnItemTrainingClickListener {
        fun onClick(trainingPlan: TrainingPlan)
    }
}
