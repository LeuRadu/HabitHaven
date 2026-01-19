package com.raduleu.habithaven.core.data.mapper

import com.raduleu.habithaven.core.data.local.entity.TaskEntity
import com.raduleu.habithaven.core.model.Difficulty
import com.raduleu.habithaven.core.model.Priority
import com.raduleu.habithaven.core.model.Task
import java.time.Instant
import java.time.ZoneId

fun TaskEntity.asDomain(): Task {
    val safePriority = try { Priority.valueOf(priority) } catch (e: Exception) { Priority.LOW }
    val safeDifficulty = try { Difficulty.valueOf(difficulty) } catch (e: Exception) { Difficulty.EASY }

    val dueLocalDate = dueDate?.let {
        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    return Task(
        id = id,
        title = title,
        completedAt = completedAt,
        dueDate = dueLocalDate,
        priority = safePriority,
        difficulty = safeDifficulty,
        focusId = focusId,
        projectId = projectId
    )
}

fun Task.asEntity(): TaskEntity {
    val dueTimestamp = dueDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()

    return TaskEntity(
        id = id,
        title = title,
        completedAt = completedAt,
        dueDate = dueTimestamp,
        priority = priority.name,
        difficulty = difficulty.name,
        focusId = focusId,
        projectId = projectId
    )
}