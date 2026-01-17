package com.raduleu.habithaven.core.data.repository

import com.raduleu.habithaven.core.model.Habit
import com.raduleu.habithaven.core.model.HabitCompletion
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun getActiveHabits(): Flow<List<Habit>>

    suspend fun getHabitById(id: String): Habit?

    suspend fun upsertHabit(habit: Habit)

    suspend fun deleteHabit(id: String)

    fun getCompletionsForHabit(habitId: String): Flow<List<HabitCompletion>>

    suspend fun addCompletion(completion: HabitCompletion)

    suspend fun removeCompletion(completionId: String)
}