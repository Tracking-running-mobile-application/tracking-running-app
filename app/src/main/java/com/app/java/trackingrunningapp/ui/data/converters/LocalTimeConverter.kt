package com.app.java.trackingrunningapp.ui.data.converters

import androidx.room.TypeConverter
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaLocalTime
import kotlinx.datetime.toKotlinLocalTime
import java.time.format.DateTimeFormatter

class LocalTimeConverter {
    private val timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME

    @TypeConverter
    fun fromLocalTime(time: LocalTime): String {
        return time.toJavaLocalTime().format(timeFormatter)
    }

    @TypeConverter
    fun toLocalTime(timeString: String): LocalTime {
        return java.time.LocalTime.parse(timeString, timeFormatter).toKotlinLocalTime()
    }
}
