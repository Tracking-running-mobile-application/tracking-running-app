package com.app.java.trackingrunningapp.model.model

data class DailyTask(
    val title: String,
    val duration: String,
    val frequency: String,
    val imageResId: Int,  // Resource ID for the task image
    var isClicked: Int = 1  // Whether the task is completed
)