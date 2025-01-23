package com.app.java.trackingrunningapp.utils

import com.app.java.trackingrunningapp.data.model.dataclass.location.Location
import com.app.java.trackingrunningapp.data.model.entity.User
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

object StatsUtils {
    fun calculateDuration(startTime: Instant, endTime: Instant = Clock.System.now()): Long {
        val durationInSeconds = endTime.epochSeconds - startTime.epochSeconds
        return durationInSeconds
    }

    fun formatDuration(seconds: Long): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60

        return if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, secs)
        } else {
            String.format("%02d:%02d", minutes, secs)
        }
    }

    fun durationToSeconds(formattedDuration: String): Long {
        val parts = formattedDuration.split(":").map { it.toInt() }
        return when (parts.size) {
            3 -> parts[0] * 3600L + parts[1] * 60L + parts[2]
            2 -> parts[0] * 60L + parts[1]
            else -> 0L
        }
    }


    fun haversineFormula(location1: Location, location2: Location): Double {
        val R = 6371e3

        val phi1 = Math.toRadians(location1.latitude)
        val phi2 = Math.toRadians(location2.latitude)
        val deltaPhi = Math.toRadians(location2.latitude - location1.latitude)
        val deltaLambda = Math.toRadians(location2.longitude - location1.longitude)

        val a = sin(deltaPhi / 2).pow(2.0) +
                cos(phi1) * cos(phi2) * sin(deltaLambda / 2).pow(2.0)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return R * c
    }

    fun convertToPace(speed: Double, unit: String): String {
        if (speed <= 0) {
            return "Invalid speed"
        }

        val paceInMinutes = 60.0 / speed

        val minutes = paceInMinutes.toInt()
        val seconds = ((paceInMinutes - minutes) * 60).toInt()

        return if (unit == User.UNIT_KM) {
            "%d:%02d min/km".format(minutes, seconds)
        } else if (unit == User.UNIT_MILE) {
            "%d:%02d min/mi".format(minutes, seconds)
        } else {
            "Invalid unit"
        }
    }

}