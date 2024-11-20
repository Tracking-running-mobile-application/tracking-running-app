package com.app.java.trackingrunningapp.ui.data.repository

import com.app.java.trackingrunningapp.ui.data.DAOs.PersonalGoalDao
import com.app.java.trackingrunningapp.ui.data.DAOs.RunSessionDao
import com.app.java.trackingrunningapp.ui.data.converters.LocalTimeConverter
import com.app.java.trackingrunningapp.ui.data.entities.PersonalGoal
import com.app.java.trackingrunningapp.ui.data.entities.RunSession
import com.app.java.trackingrunningapp.ui.utils.DateTimeUtils

class PersonalGoalRepository(
    private val personalGoalDao: PersonalGoalDao,
    private val runSessionDao: RunSessionDao
) {
    val convert = LocalTimeConverter()

    suspend fun createPersonalGoal (
        targetDistance: Double?,
        targetDuration: Double?,
        targetCaloriesBurned: Double?,
        frequency: String,
    ) {
        val currentDateString = convert.fromLocalDate(DateTimeUtils.getCurrentDate())
        val currentRunSession = runSessionDao.getCurrentRunSession()

        if (currentRunSession != null) {
            personalGoalDao.personalGoalPartialUpdate(
                goalId = 0,
                sessionId = currentRunSession.sessionId,
                targetDistance = targetDistance,
                targetDuration = targetDuration,
                targetCaloriesBurned = targetCaloriesBurned,
                frequency = frequency,
                dateCreated = currentDateString
            )
        } else {
            println("No run session found to be connected to this goal yet!");
        }
    }

    suspend fun deletePersonalGoal(goalId: Int) {
        personalGoalDao.deletePersonalGoal(goalId)
    }

    private fun calcGoalProgress(runSession: RunSession, goal: PersonalGoal): Double {
        return when {
            goal.targetDistance != null -> goal.targetDistance?.let {
                (runSession.distance / it) * 100
            } ?: 0.0
            goal.targetDuration != null -> goal.targetDuration?.let {
                (runSession.duration / it) * 100
            } ?: 0.0
            goal.targetCaloriesBurned != null -> goal.targetCaloriesBurned?.let {
                (runSession.caloriesBurned / it) * 100
            } ?: 0.0
            else -> 0.0
        }
    }

    suspend fun updateGoalProgress() {
        val currentRunSession = runSessionDao.getCurrentRunSession()

        if (currentRunSession != null) {
            val personalGoal = personalGoalDao.getPersonalGoalBySessionId(currentRunSession.sessionId)

            if (personalGoal != null) {
                val progress = calcGoalProgress(currentRunSession, personalGoal)

                personalGoalDao.updateGoalProgress(personalGoal.goalId, progress)

                if (progress >= 100.0) {
                    personalGoalDao.markGoalAchieved(personalGoal.goalId)
                }
            } else {
                println("No personal goal linked to the current run session!")
            }
        } else {
            println("No current run session available!")
        }
    }

    /*real-time update list*/
    suspend fun getAllPersonalGoals() {

    }
}