package com.raduleu.habithaven.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "focus")
data class FocusEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(), // Generates ID automatically
    val name: String,
    @ColumnInfo(name = "icon_name") val iconName: String,
    @ColumnInfo(name = "color_index") val colorIndex: Int,
    @ColumnInfo(name = "sort_order") val sortOrder: Int,
    @ColumnInfo(name = "is_archived") val isArchived: Boolean = false
)
