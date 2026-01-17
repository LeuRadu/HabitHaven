package com.raduleu.habithaven.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "projects",
    foreignKeys = [
        ForeignKey(
            entity = FocusEntity::class,
            parentColumns = ["id"],
            childColumns = ["focus_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("focus_id")]
)
data class ProjectEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "focus_id") val focusId: String,
    @ColumnInfo(name = "habit_chain_id") val habitChainId: String?,

    val title: String,
    val deadline: Long?,
    @ColumnInfo(name = "is_completed") val isCompleted: Boolean = false
)