package com.raduleu.habithaven.core.data.repository

import com.raduleu.habithaven.core.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAllTasks(): Flow<List<Task>>

    fun getTasksByFocus(focusId: String): Flow<List<Task>>

    fun getTasksByDate(start: Long, end: Long): Flow<List<Task>>

    suspend fun getTaskById(id: String): Task?

    suspend fun upsertTask(task: Task)

    suspend fun deleteTask(id: String)
}