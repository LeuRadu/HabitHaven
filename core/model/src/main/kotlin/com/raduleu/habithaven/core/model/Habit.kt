package com.raduleu.habithaven.core.model

data class Habit(
    val id: String,
    val chainId: String,
    val parentHabitId: String?,
    val focusId: String,
    val projectId: String?,
    val title: String,
    val repetitionRule: String, // Kept as JSON string for now
    val createdAt: Long,
    val difficulty: Difficulty,
    val priority: Priority,
    val retired: Boolean,
    val isNewHabit: Boolean,
    val latestStreak: Int,
    val bestStreak: Int,
    val initialStreak: Int,
    val skipped: Int,
    val lastCompletionDate: Long?,
    val lastChecked: Long,
    val lastSkip: Long?,
    val freezeIntervalDays: Int?
)