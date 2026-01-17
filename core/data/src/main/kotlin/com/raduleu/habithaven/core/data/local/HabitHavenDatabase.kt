package com.raduleu.habithaven.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.raduleu.habithaven.core.data.local.dao.FocusDao
import com.raduleu.habithaven.core.data.local.dao.HabitCompletionDao
import com.raduleu.habithaven.core.data.local.dao.HabitDao
import com.raduleu.habithaven.core.data.local.dao.ProjectDao
import com.raduleu.habithaven.core.data.local.dao.TaskDao
import com.raduleu.habithaven.core.data.local.entity.FocusEntity
import com.raduleu.habithaven.core.data.local.entity.HabitCompletionEntity
import com.raduleu.habithaven.core.data.local.entity.HabitEntity
import com.raduleu.habithaven.core.data.local.entity.ProjectEntity
import com.raduleu.habithaven.core.data.local.entity.TaskEntity

@Database(
    entities = [
        FocusEntity::class,
        ProjectEntity::class,
        TaskEntity::class,
        HabitEntity::class,
        HabitCompletionEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class HabitHavenDatabase : RoomDatabase() {
    abstract fun focusDao(): FocusDao
    abstract fun taskDao(): TaskDao
    abstract fun habitDao(): HabitDao
    abstract fun projectDao(): ProjectDao
    abstract fun habitCompletionDao(): HabitCompletionDao
}