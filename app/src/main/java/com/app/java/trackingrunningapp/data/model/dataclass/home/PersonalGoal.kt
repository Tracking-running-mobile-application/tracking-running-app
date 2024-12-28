package com.app.java.trackingrunningapp.data.model.dataclass.home

data class PersonalGoal(
    val title: String,
    val description: String,
    val percent: String = "0",
    val imageResId: Int,  // Resource ID for the task image
    var isClicked: Int = 1  // Whether the task is completed
)