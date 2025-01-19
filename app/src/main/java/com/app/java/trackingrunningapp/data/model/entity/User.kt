package com.app.java.trackingrunningapp.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 1,
    @ColumnInfo(name = "name", defaultValue = "NULL") var name: String? = null,
    @ColumnInfo(name = "age", defaultValue = "NULL")var age: Int? = null,
    @ColumnInfo(name = "height", defaultValue = "NULL") var height: Float? = null,
    @ColumnInfo(name = "weight", defaultValue = "50.0") var weight: Double? = 50.0,
    @ColumnInfo(name = "metricPreference", defaultValue = "'kg'") var metricPreference: String? = KILOGRAM,
    @ColumnInfo(name = "unit", defaultValue = "'km'") var unit: String? = UNIT_KM
) {
    companion object {
        const val UNIT_KM = "km"
        const val UNIT_MILE = "mile"
        const val KILOGRAM = "kg"
        const val POUNDS = "lbs"
    }
}
