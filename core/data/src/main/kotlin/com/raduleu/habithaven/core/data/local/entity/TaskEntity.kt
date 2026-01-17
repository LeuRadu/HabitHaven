package com.raduleu.habithaven.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = FocusEntity::class,
            parentColumns = ["id"],
            childColumns = ["focus_id"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["project_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("focus_id"), Index("project_id")]
)
data class TaskEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "focus_id") val focusId: String?,
    @ColumnInfo(name = "project_id") val projectId: String?,

    val title: String,

    @ColumnInfo(name = "completed_at") val completedAt: Long? = null,
    @ColumnInfo(name = "due_date") val dueDate: Long? = null,

    val difficulty: String,
    val priority: String
)