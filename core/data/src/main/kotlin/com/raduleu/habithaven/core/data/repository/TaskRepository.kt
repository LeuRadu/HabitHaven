package com.raduleu.habithaven.core.data.repository

import com.raduleu.habithaven.core.model.Task
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TaskRepository {

    fun getAllTasks(): Flow<List<Task>>

    fun getTasksByFocus(focusId: String): Flow<List<Task>>

    fun getTasksByDate(date: LocalDate): Flow<List<Task>>

    suspend fun getTaskById(id: String): Task?

    suspend fun upsertTask(task: Task)

    suspend fun toggleTaskCompletion(taskId: String, isCompleted: Boolean)

    suspend fun deleteTask(id: String)
}