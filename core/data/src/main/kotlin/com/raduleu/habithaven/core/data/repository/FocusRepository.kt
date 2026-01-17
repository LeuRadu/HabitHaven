package com.raduleu.habithaven.core.data.repository

import com.raduleu.habithaven.core.model.Focus
import kotlinx.coroutines.flow.Flow

interface FocusRepository {
    fun getAllFocuses(): Flow<List<Focus>>

    suspend fun getFocusById(id: String): Focus?

    suspend fun addFocus(focus: Focus)

    suspend fun updateFocus(focus: Focus)

    suspend fun deleteFocus(focus: Focus)
}