package com.app.java.trackingrunningapp.ui.data.converters

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalTime
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toKotlinLocalTime
import java.time.format.DateTimeFormatter

class LocalTimeConverter {
    private val timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun fromLocalTime(time: LocalTime): String {
        return time.toJavaLocalTime().format(timeFormatter)
    }

    @TypeConverter
    fun toLocalTime(timeString: String): LocalTime {
        return java.time.LocalTime.parse(timeString, timeFormatter).toKotlinLocalTime()
    }

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String {
        return date.toJavaLocalDate().format(dateFormatter)
    }

    fun toLocalDate(dateString: String): LocalDate {
        return java.time.LocalDate.parse(dateString, dateFormatter).toKotlinLocalDate()
    }
}
