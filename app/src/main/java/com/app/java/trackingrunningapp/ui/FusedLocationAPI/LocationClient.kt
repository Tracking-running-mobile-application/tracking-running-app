package com.app.java.trackingrunningapp.ui.FusedLocationAPI;

import kotlinx.coroutines.flow.Flow;
import android.location.Location;

internal interface LocationClient {
    fun getLocationUpdates(interval: Long): Flow<Location>

    class LocationException(message: String): Exception()
}
