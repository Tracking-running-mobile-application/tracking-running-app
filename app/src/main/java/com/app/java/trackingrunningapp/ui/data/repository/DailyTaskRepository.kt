package com.app.java.trackingrunningapp.ui.data.repository

import com.app.java.trackingrunningapp.ui.data.DAOs.RunSessionDao
import com.app.java.trackingrunningapp.ui.data.DAOs.DailyTaskDao

class DailyTaskRepository (
    private val dailyTaskDao: DailyTaskDao,
    private val runSessionDao: RunSessionDao
) {
    suspend fun calcDistanceProgress(planId: Int): Float {
        val currentRunSession = runSessionDao.getCurrentRunSession()
        val currentDailyTask = dailyTaskDao.getCurrentDailyTask()
        val distanceCovered = currentRunSession?.distance ?: 0f
        val targetDistance = currentDailyTask?.targetDistance ?: 0f
        var distanceProgress = currentDailyTask?.distanceProgress ?: 0f

        if (distanceCovered > 0f) {
            distanceProgress = (distanceCovered / targetDistance) * 100
        } else {
            println("Warning: This run is not being attached to any run session yet!")
        }

        dailyTaskDao.updateDistanceProgress(planId, distanceProgress)

        return distanceProgress
    }

    suspend fun finishDailyTask(planId: Int) {
        dailyTaskDao.finishDailyTask(planId)
    }

    suspend fun showDailyTask(planId: Int) {
        dailyTaskDao.showDailyTask(planId)
    }

    suspend fun hideDailyTask(planId: Int) {
        dailyTaskDao.hideDailyTask(planId)
    }

    suspend fun startDailyTask(planId: Int) {
        dailyTaskDao.activeDailyTask(planId)
    }

    suspend fun stopDailyTask(planId: Int) {
        dailyTaskDao.unactiveDailyTask(planId)
    }
}