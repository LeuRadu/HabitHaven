package com.raduleu.habithaven.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.raduleu.habithaven.core.data.local.entity.HabitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Query("SELECT * FROM habits WHERE retired = 0 ORDER BY sort_order ASC, created_at DESC")
    fun getActiveHabits(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits WHERE focus_id = :focusId AND retired = 0 ORDER BY sort_order ASC")
    fun getActiveHabitsByFocus(focusId: String): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getHabitById(id: String): HabitEntity?

    @Query("SELECT * FROM habits WHERE chain_id = :chainId ORDER BY created_at DESC")
    fun getHabitChain(chainId: String): Flow<List<HabitEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertHabit(habit: HabitEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertHabit(habit: HabitEntity)

    @Delete
    suspend fun deleteHabit(habit: HabitEntity)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteHabitById(id: String)

    @Query("UPDATE habits SET retired = 1 WHERE id = :id")
    suspend fun archiveHabit(id: String)

    @Query("UPDATE habits SET latest_streak = :latest, best_streak = :best, last_completion_date = :lastDate WHERE id = :id")
    suspend fun updateStreakStats(id: String, latest: Int, best: Int, lastDate: Long?)

    @Transaction
    suspend fun updateHabit(oldHabitId: String, newHabit: HabitEntity) {
        archiveHabit(oldHabitId)
        //TODO: must ensure all new habits generate a new id or it will fail
        insertHabit(newHabit)
    }
}