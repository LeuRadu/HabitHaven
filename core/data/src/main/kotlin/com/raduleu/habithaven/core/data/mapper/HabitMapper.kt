package com.raduleu.habithaven.core.data.mapper

import com.raduleu.habithaven.core.data.local.entity.HabitCompletionEntity
import com.raduleu.habithaven.core.data.local.entity.HabitEntity
import com.raduleu.habithaven.core.model.Difficulty
import com.raduleu.habithaven.core.model.Habit
import com.raduleu.habithaven.core.model.HabitCompletion
import com.raduleu.habithaven.core.model.Priority

fun HabitEntity.asDomain(): Habit {
    return Habit(
        id = id,
        chainId = chainId,
        parentHabitId = parentHabitId,
        focusId = focusId,
        projectId = projectId,
        title = title,
        repetitionRule = repetitionRule, // JSON String passed through
        createdAt = createdAt,
        difficulty = try { Difficulty.valueOf(difficulty) } catch (e: Exception) { Difficulty.MEDIUM },
        priority = try { Priority.valueOf(priority) } catch (e: Exception) { Priority.MEDIUM },
        retired = retired,
        isNewHabit = isNewHabit,
        latestStreak = latestStreak,
        bestStreak = bestStreak,
        initialStreak = initialStreak,
        skipped = skipped,
        lastCompletionDate = lastCompletionDate,
        lastChecked = lastChecked,
        lastSkip = lastSkip,
        freezeIntervalDays = freezeIntervalDays
    )
}

fun Habit.asEntity(): HabitEntity {
    return HabitEntity(
        id = id,
        chainId = chainId,
        parentHabitId = parentHabitId,
        focusId = focusId,
        projectId = projectId,
        title = title,
        repetitionRule = repetitionRule,
        createdAt = createdAt,
        difficulty = difficulty.name,
        priority = priority.name,
        retired = retired,
        isNewHabit = isNewHabit,
        latestStreak = latestStreak,
        bestStreak = bestStreak,
        initialStreak = initialStreak,
        skipped = skipped,
        lastCompletionDate = lastCompletionDate,
        lastChecked = lastChecked,
        lastSkip = lastSkip,
        freezeIntervalDays = freezeIntervalDays,
    )
}

fun HabitCompletionEntity.asDomain(): HabitCompletion {
    return HabitCompletion(
        id = id,
        habitId = habitId,
        chainId = chainId,
        timestamp = timestamp,
        value = value
    )
}

fun HabitCompletion.asEntity(): HabitCompletionEntity {
    return HabitCompletionEntity(
        habitId = habitId,
        chainId = chainId,
        timestamp = timestamp,
        value = value,
    )
}