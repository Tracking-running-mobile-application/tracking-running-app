package com.app.java.trackingrunningapp.data.model.entity.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("User")
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("user_id")val userId: Int = 1,
    @ColumnInfo("name") val name: String = "Thang Tran",
    @ColumnInfo("age") val age: Int? = null,
    @ColumnInfo("height") val height: Double? = 150.0, // cm
    @ColumnInfo("weight") val weight: Double? = 50.0 // kg
)

