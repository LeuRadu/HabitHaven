package com.raduleu.habithaven.core.data.repository

import com.raduleu.habithaven.core.data.local.dao.TaskDao
import com.raduleu.habithaven.core.data.mapper.asDomain
import com.raduleu.habithaven.core.data.mapper.asEntity
import com.raduleu.habithaven.core.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class OfflineTaskRepository @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {

    override fun getTasksByDate(date: LocalDate): Flow<List<Task>> {
        val startOfDay = date.atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        return taskDao.getAllActiveTasks(startOfToday = startOfDay)
            .map { list -> list.map { it.asDomain() } }
    }

    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks()
            .map { list -> list.map { it.asDomain() } }
    }

    override fun getTasksByFocus(focusId: String): Flow<List<Task>> {
        val startOfToday = LocalDate.now()
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        return taskDao.getActiveTasksByFocus(focusId, startOfToday)
            .map { list -> list.map { it.asDomain() } }
    }

    override suspend fun getTaskById(id: String): Task? {
        return taskDao.getTaskById(id)?.asDomain()
    }

    override suspend fun upsertTask(task: Task) {
        taskDao.upsertTask(task.asEntity())
    }

    override suspend fun toggleTaskCompletion(taskId: String, isCompleted: Boolean) {
        taskDao.getTaskById(taskId)?.let {
            val timestamp = if (isCompleted) System.currentTimeMillis() else null
            taskDao.updateCompletionStatus(taskId, timestamp)
        }
    }

    override suspend fun deleteTask(id: String) {
        taskDao.deleteTaskById(id)
    }
}