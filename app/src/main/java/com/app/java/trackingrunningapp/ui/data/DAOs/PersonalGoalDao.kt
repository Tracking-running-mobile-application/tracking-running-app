package com.app.java.trackingrunningapp.ui.data.DAOs

import androidx.compose.ui.geometry.Offset
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.app.java.trackingrunningapp.ui.data.entities.PersonalGoal
import com.app.java.trackingrunningapp.ui.data.entities.RunSession
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonalGoalDao {
    @Query(
        """
        SELECT * 
        FROM PersonalGoal 
        ORDER BY dateCreated DESC
        """)
    fun getAllPersonalGoals(): List<PersonalGoal>

    @Query ("UPDATE PersonalGoal SET isAchieved = TRUE WHERE goalId = :goalId")
    suspend fun markGoalAchieved(goalId: Int)

    @Query ("UPDATE PersonalGoal SET isAchieved = FALSE WHERE goalId = :goalId")
    suspend fun markGoalUnachieved(goalId: Int)

    @Upsert
    suspend fun upsertPersonalGoal(personalGoal: PersonalGoal)

    @Query("UPDATE PersonalGoal SET goalProgress = :goalProgress WHERE goalId = :goalId")
    suspend fun updateGoalProgress(goalId: Int, goalProgress: Double)

    @Query("SELECT * FROM PersonalGoal WHERE sessionId = :sessionId LIMIT 1")
    suspend fun getPersonalGoalBySessionId(sessionId: Int): PersonalGoal?

    @Delete
    suspend fun deletePersonalGoal(goalId: Int)
}