package com.app.java.trackingrunningapp.data.repository

import com.app.java.trackingrunningapp.data.dao.PersonalGoalDao
import com.app.java.trackingrunningapp.data.model.entity.goal.PersonalGoal

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
    suspend fun deletePersonalGoal(personalGoal: PersonalGoal){
        personalGoalDao.deletePersonalGoal(personalGoal)
    }
}