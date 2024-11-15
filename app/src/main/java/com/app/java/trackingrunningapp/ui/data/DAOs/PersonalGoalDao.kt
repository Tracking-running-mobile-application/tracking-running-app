package com.app.java.trackingrunningapp.ui.data.DAOs

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.app.java.trackingrunningapp.ui.data.entities.PersonalGoal
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonalGoalDao {
    @Query("SELECT * FROM PersonalGoal")
    fun getPersonalGoal(): Flow<List<PersonalGoal>>

    @Query ("UPDATE PersonalGoal SET targetDistance = :targetDistance WHERE goalId = :goalId")
    suspend fun setTargetDistance(goalId: Int, targetDistance: Float )

    @Query ("UPDATE PersonalGoal SET targetDuration = :targetDuration WHERE goalId = :goalId")
    suspend fun setTargetDuration(goalId: Int, targetDuration: Float)

    @Query ("UPDATE PersonalGoal SET frequency = :frequency WHERE goalId = :goalId")
    suspend fun setFrequency(goalId: Int, frequency: String)

    @Query ("UPDATE PersonalGoal SET isAchieved = :isAchieved WHERE goalId = :goalId")
    suspend fun markGoalAchieved(goalId: Int, isAchieved: Boolean)

    @Query (""" 
        UPDATE PersonalGoal
        SET 
            targetDuration = :targetDuration,
            targetDistance = :targetDistance,
            frequency = :frequency
        WHERE
            goalId = :goalId
            """)
    suspend fun personalGoalPartialUpdate(goalId: Int, targetDistance: Float?, targetDuration: Float?, frequency: String?)
}