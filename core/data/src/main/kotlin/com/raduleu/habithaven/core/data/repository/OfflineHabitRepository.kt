package com.raduleu.habithaven.core.data.repository

import androidx.room.withTransaction
import com.raduleu.habithaven.core.data.local.HabitHavenDatabase
import com.raduleu.habithaven.core.data.local.dao.HabitCompletionDao
import com.raduleu.habithaven.core.data.local.dao.HabitDao
import com.raduleu.habithaven.core.data.mapper.asDomain
import com.raduleu.habithaven.core.data.mapper.asEntity
import com.raduleu.habithaven.core.model.Habit
import com.raduleu.habithaven.core.model.HabitCompletion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineHabitRepository @Inject constructor(
    private val habitDao: HabitDao,
    private val completionDao: HabitCompletionDao,
    private val database: HabitHavenDatabase
) : HabitRepository {
    override suspend fun getHabitById(id: String): Habit? {
        return habitDao.getHabitById(id)?.asDomain()
    }

    override fun getAllActiveHabits(): Flow<List<Habit>> {
        return habitDao.getActiveHabits().map { habits ->
            habits.map { it.asDomain() }
        }
    }

    override fun getAllCompletions(): Flow<List<HabitCompletion>> {
        return completionDao.getAllCompletions().map { completions ->
            completions.map { it.asDomain() }
        }
    }

    override fun getCompletionsForFocus(focusId: String): Flow<List<HabitCompletion>> {
        return completionDao.getCompletionsForFocus(focusId).map { completions ->
            completions.map { it.asDomain() }
        }
    }

    override fun getCompletionsForHabit(habitChainId: String): Flow<List<HabitCompletion>> {
        return completionDao.getCompletionsForHabit(habitChainId).map { completions ->
            completions.map { it.asDomain() }
        }
    }

    //TODO: replace this with a query for last completion
    override fun getLatestCompletionForHabit(habitChainId: String): Flow<HabitCompletion?> {
        return completionDao.getCompletionsForHabit(habitChainId).map { completions ->
            completions.lastOrNull()?.asDomain()
        }
    }

    override suspend fun createHabit(habit: Habit) {
        habitDao.upsertHabit(habit.asEntity())
    }

    override suspend fun updateHabit(
        oldHabitId: String,
        newHabit: Habit
    ) {
        habitDao.updateHabit(oldHabitId, newHabit.asEntity())
    }

    override suspend fun archiveHabit(habitId: String) {
        habitDao.archiveHabit(habitId)
    }

    //TODO: ensure the habit update logic is solid
    override suspend fun addHabitCompletion(habit: Habit, completion: HabitCompletion) {
        database.withTransaction {
            habitDao.upsertHabit(habit.asEntity())
            completionDao.insertCompletion(completion.asEntity())
        }
    }

    //TODO: ensure habit update logic is solid, taking into account parent habit
    override suspend fun removeCompletion(habit: Habit, completion: HabitCompletion) {
        database.withTransaction {
            habitDao.upsertHabit(habit.asEntity())
            completionDao.deleteCompletion(completion.asEntity())
        }
    }

}