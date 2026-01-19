package com.raduleu.habithaven.core.data.mapper

import com.raduleu.habithaven.core.data.local.entity.TaskEntity
import com.raduleu.habithaven.core.model.Difficulty
import com.raduleu.habithaven.core.model.Priority
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

class TaskMapperTest {

    @Test
    fun `asDomain maps valid entity correctly`() {
        val entity = TaskEntity(
            id = "1",
            title = "Test",
            priority = "HIGH",
            difficulty = "HARD",
            dueDate = LocalDate.of(2024, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            completedAt = 12345L,
            focusId = null, projectId = null
        )

        val domain = entity.asDomain()

        assertEquals(Priority.HIGH, domain.priority)
        assertEquals(Difficulty.HARD, domain.difficulty)
        assertEquals(LocalDate.of(2024, 1, 1), domain.dueDate)
        assertEquals(12345L, domain.completedAt)
    }

    @Test
    fun `asDomain handles invalid enums gracefully`() {
        // Simulating data corruption or old version
        val entity = TaskEntity(
            id = "1", title = "Test",
            priority = "MEGA_HIGH_INVALID", // Invalid
            difficulty = "IMPOSSIBLE",      // Invalid
            focusId = null, projectId = null
        )

        val domain = entity.asDomain()

        // Should fall back to defaults, NOT crash
        assertEquals(Priority.LOW, domain.priority)
        assertEquals(Difficulty.EASY, domain.difficulty)
    }

    @Test
    fun `asEntity converts LocalDate back to timestamp`() {
        val date = LocalDate.of(2024, 5, 20)
        val domain = com.raduleu.habithaven.core.model.Task(
            id = "1", title = "Test", completedAt = null,
            dueDate = date,
            priority = Priority.MEDIUM, difficulty = Difficulty.MEDIUM,
            focusId = null, projectId = null
        )

        val entity = domain.asEntity()

        val expectedTimestamp = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        assertEquals(expectedTimestamp, entity.dueDate)
    }
}