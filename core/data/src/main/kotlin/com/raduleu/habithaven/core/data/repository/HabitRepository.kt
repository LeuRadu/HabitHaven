package com.raduleu.habithaven.core.data.repository

import com.raduleu.habithaven.core.model.Habit
import com.raduleu.habithaven.core.model.HabitCompletion
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    // --- Reads ---
    suspend fun getHabitById(id: String): Habit?
    fun getAllActiveHabits(): Flow<List<Habit>>

    // --- Calendar/Streak Support ---
    fun getAllCompletions(): Flow<List<HabitCompletion>>
    fun getCompletionsForFocus(focusId: String): Flow<List<HabitCompletion>>
    fun getCompletionsForHabit(habitChainId: String): Flow<List<HabitCompletion>>
    fun getLatestCompletionForHabit(habitChainId: String): Flow<HabitCompletion?>

    // --- Writes ---
    suspend fun createHabit(habit: Habit)
    suspend fun updateHabit(oldHabitId: String, newHabit: Habit)
    suspend fun archiveHabit(habitId: String)

    // Toggles completion and updates persisted streak (Method B)
    suspend fun addHabitCompletion(habit: Habit, completion: HabitCompletion)
    suspend fun removeCompletion(habit: Habit, completion: HabitCompletion)
}