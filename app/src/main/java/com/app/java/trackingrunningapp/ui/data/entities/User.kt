package com.app.java.trackingrunningapp.ui.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int? = 0,
    val name: String?,
    val age: Int?,
    val gender: String? = User.female,
    val height: Float?,
    val weight: Float?,
    var unit: String? = User.UNIT_MILE
) {
    companion object {
        const val UNIT_KM = "km"
        const val UNIT_MILE = "mile"
        const val male = "male"
        const val female = "female"
    }
}
