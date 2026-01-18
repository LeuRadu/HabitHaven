package com.raduleu.habithaven.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.raduleu.habithaven.core.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("""
        SELECT * FROM tasks 
        WHERE completed_at IS NULL OR completed_at >= :startOfToday
        ORDER BY completed_at ASC, due_date ASC
    """)
    fun getAllActiveTasks(startOfToday: Long): Flow<List<TaskEntity>>

    @Query("""
        SELECT * FROM tasks 
        WHERE focus_id = :focusId 
        AND (completed_at IS NULL OR completed_at >= :startOfToday)
        ORDER BY completed_at ASC, due_date ASC
    """)
    fun getTasksByFocus(focusId: String, startOfToday: Long): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: String): TaskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTaskById(id: String)
}