package com.raduleu.habithaven.core.data.repository

import com.raduleu.habithaven.core.data.local.dao.FocusDao
import com.raduleu.habithaven.core.data.mapper.asDomain
import com.raduleu.habithaven.core.data.mapper.asEntity
import com.raduleu.habithaven.core.model.Focus
import com.raduleu.habithaven.core.model.FocusWithChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineFocusRepository @Inject constructor(
    private val focusDao: FocusDao
) : FocusRepository {

    override fun getActiveFocusesWithChildren(): Flow<List<FocusWithChildren>> {
        return focusDao.getActiveFocusesWithChildren().map { entities ->
            entities.map { FocusWithChildren(
                focus = it.focus.asDomain(),
                tasks = it.tasks.map { task -> task.asDomain() },
                habits = it.habits.map { habit -> habit.asDomain() }
            ) }
        }
    }

    override fun getActiveFocuses(): Flow<List<Focus>> {
        return focusDao.getActiveFocuses().map { entities ->
            entities.map { it.asDomain() }
        }
    }

    override suspend fun getFocusById(id: String): Focus? {
        return focusDao.getFocusById(id)?.asDomain()
    }

    override suspend fun upsertFocus(focus: Focus) {
        focusDao.upsertFocus(focus.asEntity())
    }

    override suspend fun deleteFocus(id: String) {
        focusDao.deleteFocusById(id)
    }

    override suspend fun archiveFocus(id: String) {
        focusDao.archiveFocus(id)
    }
}