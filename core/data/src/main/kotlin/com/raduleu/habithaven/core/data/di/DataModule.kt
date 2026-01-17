package com.raduleu.habithaven.core.data.di

import android.content.Context
import androidx.room.Room
import com.raduleu.habithaven.core.data.local.HabitHavenDatabase
import com.raduleu.habithaven.core.data.local.dao.FocusDao
import com.raduleu.habithaven.core.data.local.dao.HabitCompletionDao
import com.raduleu.habithaven.core.data.local.dao.HabitDao
import com.raduleu.habithaven.core.data.local.dao.ProjectDao
import com.raduleu.habithaven.core.data.local.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideHabitHavenDatabase(
        @ApplicationContext context: Context
    ): HabitHavenDatabase {
        return Room.databaseBuilder(
            context,
            HabitHavenDatabase::class.java,
            "habithaven-database"
        ).build()
    }

    @Provides
    fun provideFocusDao(database: HabitHavenDatabase): FocusDao = database.focusDao()

    @Provides
    fun provideTaskDao(database: HabitHavenDatabase): TaskDao = database.taskDao()

    @Provides
    fun provideHabitDao(database: HabitHavenDatabase): HabitDao = database.habitDao()

    @Provides
    fun provideProjectDao(database: HabitHavenDatabase): ProjectDao = database.projectDao()

    @Provides
    fun provideHabitCompletionDao(database: HabitHavenDatabase): HabitCompletionDao = database.habitCompletionDao()
}