package com.raduleu.habithaven.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.raduleu.habithaven.core.data.local.entity.HabitCompletionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitCompletionDao {

    @Query("SELECT * FROM habit_completions")
    fun getAllCompletions(): Flow<List<HabitCompletionEntity>>

    @Query("SELECT * FROM habit_completions WHERE habit_id = :habitId ORDER BY timestamp DESC")
    fun getCompletionsForHabit(habitId: String): Flow<List<HabitCompletionEntity>>

    @Query("SELECT * FROM habit_completions WHERE chain_id = :chainId ORDER BY timestamp DESC")
    fun getCompletionsForHabitChain(chainId: String): Flow<List<HabitCompletionEntity>>

    @Query("""
    SELECT C.* FROM habit_completions AS C
    INNER JOIN habits AS H ON C.habit_id = H.id
    WHERE H.focus_id = :focusId
""")
    fun getCompletionsForFocus(focusId: String): Flow<List<HabitCompletionEntity>>

    @Query("""
        SELECT * FROM habit_completions 
        WHERE timestamp >= :start AND timestamp <= :end 
        ORDER BY timestamp DESC
    """)
    fun getCompletionsBetween(start: Long, end: Long): Flow<List<HabitCompletionEntity>>

    @Query("""
        SELECT COUNT(*) FROM habit_completions 
        WHERE chain_id = :chainId AND timestamp >= :startOfToday
    """)
    suspend fun getCountForToday(chainId: String, startOfToday: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompletion(completion: HabitCompletionEntity)

    @Delete
    suspend fun deleteCompletion(completion: HabitCompletionEntity)

    @Query("DELETE FROM habit_completions WHERE id = :id")
    suspend fun deleteCompletionById(id: String)
}