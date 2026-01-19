package com.raduleu.habithaven.core.model

import java.time.LocalDate

data class Task(
    val id: String,
    val focusId: String?,
    val projectId: String?,
    val title: String,
    val completedAt: Long?,
    val dueDate: LocalDate?,
    val difficulty: Difficulty,
    val priority: Priority
)

