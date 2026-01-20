package com.raduleu.habithaven.core.data.repository

import com.raduleu.habithaven.core.model.Focus
import com.raduleu.habithaven.core.model.FocusWithChildren
import kotlinx.coroutines.flow.Flow

interface FocusRepository {
    fun getActiveFocusesWithChildren(): Flow<List<FocusWithChildren>>
    fun getActiveFocuses(): Flow<List<Focus>>
    suspend fun getFocusById(id: String): Focus?

    suspend fun upsertFocus(focus: Focus)
    suspend fun deleteFocus(id: String)
    suspend fun archiveFocus(id: String)
}