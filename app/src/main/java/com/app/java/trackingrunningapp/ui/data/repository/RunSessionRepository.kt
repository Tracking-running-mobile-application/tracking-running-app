package com.app.java.trackingrunningapp.ui.data.repository

import com.app.java.trackingrunningapp.ui.data.DAOs.RunSessionDao
import com.app.java.trackingrunningapp.ui.data.entities.RunSession
import kotlinx.coroutines.flow.Flow

class RunSessionRepository(private val runSessionDao: RunSessionDao) {
    suspend fun filterRunningSessionByDay(startDate: String, endDate: String): Flow<List<RunSession>> {
        return runSessionDao.filterRunningSessionByDay(startDate, endDate)
    }

}