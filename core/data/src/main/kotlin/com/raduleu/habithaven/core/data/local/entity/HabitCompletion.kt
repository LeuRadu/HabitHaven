package com.raduleu.habithaven.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "habit_completions",
    foreignKeys = [
        ForeignKey(
            entity = HabitEntity::class,
            parentColumns = ["id"],
            childColumns = ["habit_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    // We index 'timestamp' because you will frequently query ranges
    indices = [Index("habit_id"), Index("chain_id"), Index("timestamp")]
)
data class HabitCompletionEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "habit_id") val habitId: String,
    @ColumnInfo(name = "chain_id") val chainId: String,

    val timestamp: Long,
    // Future proofing: If you track minutes, this stores the number.
    val value: Int = 1
)
