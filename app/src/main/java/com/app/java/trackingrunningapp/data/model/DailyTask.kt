package com.app.java.trackingrunningapp.data.model

data class DailyTask(
    val title: String,
    val duration: String,
    val frequency: String,
    val imageResId: Int,  // Resource ID for the task image
    val isChecked: Boolean  // Whether the task is completed
)