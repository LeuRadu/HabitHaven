package com.raduleu.habithaven.core.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class FocusAggregate(
    @Embedded val focus: FocusEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "focus_id"
    )
    val tasks: List<TaskEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "focus_id"
    )
    val habits: List<HabitEntity>
)