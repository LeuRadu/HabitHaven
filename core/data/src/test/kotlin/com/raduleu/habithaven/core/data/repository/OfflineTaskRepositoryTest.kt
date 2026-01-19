package com.raduleu.habithaven.core.data.repository

import com.raduleu.habithaven.core.data.local.dao.TaskDao
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Test

class OfflineTaskRepositoryTest {

    private val taskDao = mockk<TaskDao>(relaxed = true)
    private val repository = OfflineTaskRepository(taskDao)

    @Test
    fun `toggleTaskCompletion true sends current timestamp`() = runTest {
        repository.toggleTaskCompletion("task-1", isCompleted = true)

        coVerify {
            taskDao.updateCompletionStatus(
                taskId = "task-1",
                completedAt = withArg { timestamp ->
                    assertNotNull("Should send a timestamp", timestamp)
                }
            )
        }
    }

    @Test
    fun `toggleTaskCompletion false sends null`() = runTest {
        repository.toggleTaskCompletion("task-1", isCompleted = false)

        coVerify { taskDao.updateCompletionStatus("task-1", null) }
    }
}