package com.app.java.trackingrunningapp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.app.java.trackingrunningapp.data.model.entity.PersonalGoal

@Dao
interface PersonalGoalDao {
    @Query(
        """
        SELECT * 
        FROM PersonalGoal 
        ORDER BY dateCreated DESC
        """)
    fun getAllPersonalGoals(): List<PersonalGoal>

    @Query("SELECT goalProgress FROM PersonalGoal WHERE goalId = :goalId")
    suspend fun getGoalProgress(goalId: Int): Double

    @Upsert
    suspend fun upsertPersonalGoal(personalGoal: PersonalGoal)

    @Query("UPDATE PersonalGoal SET goalProgress = :goalProgress WHERE goalId = :goalId")
    suspend fun updateGoalProgress(goalId: Int, goalProgress: Double)

    @Query("SELECT * FROM PersonalGoal WHERE goalSessionId = :goalSessionId LIMIT 1")
    suspend fun getPersonalGoalBySessionId(goalSessionId: Int): PersonalGoal?

    @Query("DELETE FROM PersonalGoal WHERE goalId = :goalId")
    suspend fun deletePersonalGoal(goalId: Int)

    @Query("UPDATE PersonalGoal SET goalSessionId = :sessionId WHERE goalId = :goalId")
    suspend fun setSessionForPersonalGoal(goalId: Int, sessionId: Int)
}