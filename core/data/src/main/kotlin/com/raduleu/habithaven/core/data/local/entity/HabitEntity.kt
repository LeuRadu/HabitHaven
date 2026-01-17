package com.raduleu.habithaven.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "habits",
    foreignKeys = [
        ForeignKey(
            entity = FocusEntity::class,
            parentColumns = ["id"],
            childColumns = ["focus_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["project_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("focus_id"), Index("project_id"), Index("chain_id")]
)
data class HabitEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "chain_id") val chainId: String,
    @ColumnInfo(name = "parent_habit_id") val parentHabitId: String?,

    @ColumnInfo(name = "focus_id") val focusId: String,
    @ColumnInfo(name = "project_id") val projectId: String?,

    val title: String,

    @ColumnInfo(name = "repetition_rule") val repetitionRule: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,

    val difficulty: String,
    val priority: String,

    val retired: Boolean = false,
    @ColumnInfo(name = "is_new_habit") val isNewHabit: Boolean = true,

    @ColumnInfo(name = "latest_streak") val latestStreak: Int = 0,
    @ColumnInfo(name = "best_streak") val bestStreak: Int = 0,
    @ColumnInfo(name = "initial_streak") val initialStreak: Int = 0,
    val skipped: Int = 0,
    @ColumnInfo(name = "last_completion_date") val lastCompletionDate: Long? = null,

    // Forgiveness Logic
    @ColumnInfo(name = "last_checked") val lastChecked: Long,
    @ColumnInfo(name = "last_skip") val lastSkip: Long?,
    @ColumnInfo(name = "freeze_interval_days") val freezeIntervalDays: Int?
)
