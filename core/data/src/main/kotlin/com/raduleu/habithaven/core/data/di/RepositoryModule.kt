package com.raduleu.habithaven.core.data.di

import com.raduleu.habithaven.core.data.repository.FocusRepository
import com.raduleu.habithaven.core.data.repository.OfflineFocusRepository
import com.raduleu.habithaven.core.data.repository.OfflineTaskRepository
import com.raduleu.habithaven.core.data.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFocusRepository(
        repository: OfflineFocusRepository
    ): FocusRepository

    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        repository: OfflineTaskRepository
    ): TaskRepository
}