package com.app.java.trackingrunningapp.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 1,
    @ColumnInfo(name = "name", defaultValue = "NULL") val name: String? = null,
    @ColumnInfo(name = "age", defaultValue = "NULL")val age: Int? = null,
    @ColumnInfo(name = "height", defaultValue = "NULL") val height: Float? = null,
    @ColumnInfo(name = "weight", defaultValue = "50.0") val weight: Double? = 50.0,
    @ColumnInfo(name = "metricPreference", defaultValue = "'kg'") val metricPreference: String? = KILOGRAM,
    @ColumnInfo(name = "unit", defaultValue = "'km'") var unit: String? = UNIT_KM
) {
    companion object {
        const val UNIT_KM = "km"
        const val UNIT_MILE = "mile"
        const val KILOGRAM = "kg"
        const val POUNDS = "lbs"
    }
}
