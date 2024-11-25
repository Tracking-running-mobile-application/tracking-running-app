package com.app.java.trackingrunningapp.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int? = 0,
    val name: String?,
    val age: Int?,
    val height: Float?,
    @ColumnInfo(name = "weight", defaultValue = "50.0") val weight: Float = 50f,
    @ColumnInfo(name = "metricPreference", defaultValue = "'kg'") var metricPreference: String? = User.KILOGRAM,
    @ColumnInfo(name = "unit", defaultValue = "'km'") var unit: String? = User.UNIT_KM
) {
    companion object {
        const val UNIT_KM = "km"
        const val UNIT_MILE = "mile"
        const val KILOGRAM = "kg"
        const val POUNDS = "lbs"
    }
}
