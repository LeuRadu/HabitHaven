package com.raduleu.habithaven.core.data.local.dao

import androidx.room.Embedded
import androidx.room.Relation
import com.raduleu.habithaven.core.data.local.entity.FocusEntity
import com.raduleu.habithaven.core.data.local.entity.HabitEntity
import com.raduleu.habithaven.core.data.local.entity.TaskEntity

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