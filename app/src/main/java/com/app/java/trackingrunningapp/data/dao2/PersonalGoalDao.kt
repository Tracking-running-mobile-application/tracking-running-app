package com.app.java.trackingrunningapp.data.dao2

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.app.java.trackingrunningapp.data.model.entity.goal.PersonalGoal

@Dao
interface PersonalGoalDao {
    @Query(
        """
        SELECT * 
        FROM PersonalGoal 
        ORDER BY dateCreated DESC
        """)
    fun getAllPersonalGoals(): List<PersonalGoal>

    @Query("UPDATE PersonalGoal SET is_achieved = TRUE WHERE goal_id = :goalId")
    suspend fun markGoalAchieved(goalId: Int)

    @Query("SELECT goal_progress FROM PersonalGoal WHERE goal_id = :goalId")
    suspend fun getGoalProgress(goalId: Int): Double

    @Update
    suspend fun updatePersonalGoal(personalGoal: PersonalGoal)

    @Query("UPDATE PersonalGoal SET goal_progress = :goalProgress WHERE goal_id = :goalId")
    suspend fun updateGoalProgress(goalId: Int, goalProgress: Double)

    @Query("SELECT * FROM PersonalGoal WHERE goal_session_id = :goalSessionId LIMIT 1")
    suspend fun getPersonalGoalBySessionId(goalSessionId: Int): PersonalGoal?

    @Query("DELETE FROM PersonalGoal WHERE goal_id = :goalId")
    suspend fun deletePersonalGoal(goalId: Int)
}