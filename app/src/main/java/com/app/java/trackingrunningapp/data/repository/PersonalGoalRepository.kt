package com.app.java.trackingrunningapp.data.repository

import com.app.java.trackingrunningapp.data.dao2.PersonalGoalDao
import com.app.java.trackingrunningapp.data.dao2.RunSessionDao
import com.app.java.trackingrunningapp.data.model.entity.goal.PersonalGoal
import com.app.java.trackingrunningapp.data.model.entity.goal.RunSession
import com.app.java.trackingrunningapp.utils.DateTimeUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

class PersonalGoalRepository(
    private val personalGoalDao: PersonalGoalDao
) {
    fun getAllPersonalGoals(): List<PersonalGoal>{
        return personalGoalDao.getAllPersonalGoals()
    }

    suspend fun markGoalAchieved(goalId: Int){
        personalGoalDao.markGoalAchieved(goalId)
    }

    suspend fun getGoalProgress(goalId: Int): Double{
        return personalGoalDao.getGoalProgress(goalId)
    }

    suspend fun updatePersonalGoal(personalGoal: PersonalGoal){
        personalGoalDao.updatePersonalGoal(personalGoal)
    }
    suspend fun updateGoalProgress(goalId: Int, goalProgress: Double){
        personalGoalDao.updateGoalProgress(goalId,goalProgress)
    }
    suspend fun getPersonalGoalBySessionId(goalSessionId: Int): PersonalGoal?{
        return personalGoalDao.getPersonalGoalBySessionId(goalSessionId)
    }
    suspend fun deletePersonalGoal(goalId: Int){
        personalGoalDao.deletePersonalGoal(goalId)
    }
}