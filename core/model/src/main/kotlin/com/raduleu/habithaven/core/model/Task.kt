package com.raduleu.habithaven.core.model

enum class TaskDifficulty { EASY, MEDIUM, HARD, MILESTONE }
enum class TaskPriority { LOW, MEDIUM, HIGH }

data class Task(
    val id: String,
    val focusId: String?,
    val projectId: String?,
    val title: String,
    val completedAt: Long?,
    val dueDate: Long?,
    val difficulty: TaskDifficulty,
    val priority: TaskPriority
)
