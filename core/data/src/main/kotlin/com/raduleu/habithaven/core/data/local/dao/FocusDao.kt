package com.raduleu.habithaven.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.raduleu.habithaven.core.data.local.entity.FocusAggregate
import com.raduleu.habithaven.core.data.local.entity.FocusEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FocusDao {
    @Transaction
    @Query("SELECT * FROM focus WHERE is_archived = 0 ORDER BY sort_order ASC")
    fun getActiveFocusesWithChildren(): Flow<List<FocusAggregate>>

    @Query("SELECT * FROM focus WHERE is_archived = 0 ORDER BY sort_order ASC")
    fun getActiveFocuses(): Flow<List<FocusEntity>>

    @Query("SELECT COUNT(*) FROM tasks WHERE focus_id = :focusId")
    suspend fun getTaskCount(focusId: String): Int

    @Query("SELECT COUNT(*) FROM habits WHERE focus_id = :focusId")
    suspend fun getHabitCount(focusId: String): Int

    @Query("SELECT * FROM focus WHERE id = :id")
    suspend fun getFocusById(id: String): FocusEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertFocus(focus: FocusEntity)

    @Query("DELETE FROM focus WHERE id = :id")
    suspend fun deleteFocusById(id: String)

    //TODO: consider archive feature - let user choose if they keep habits/tasks
    @Query("UPDATE focus SET is_archived = 1 WHERE id = :id")
    suspend fun archiveFocus(id: String)
}