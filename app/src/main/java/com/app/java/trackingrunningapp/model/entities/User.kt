package com.app.java.trackingrunningapp.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int? = 0,
    val name: String?,
    val age: Int?,
    val height: Float?,
    val weight: Float = 50f,
    var metricPreference: String? = User.KILOGRAM,
    var unit: String? = User.UNIT_KM
) {
    companion object {
        const val UNIT_KM = "km"
        const val UNIT_MILE = "mile"
        const val KILOGRAM = "kg"
        const val POUNDS = "lbs"
    }
}
