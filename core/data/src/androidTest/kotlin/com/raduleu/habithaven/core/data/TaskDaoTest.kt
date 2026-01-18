package com.raduleu.habithaven.core.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.raduleu.habithaven.core.data.local.HabitHavenDatabase
import com.raduleu.habithaven.core.data.local.dao.TaskDao
import com.raduleu.habithaven.core.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import java.time.ZoneId
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class TaskDaoTest {

    private lateinit var db: HabitHavenDatabase
    private lateinit var dao: TaskDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, HabitHavenDatabase::class.java).build()
        dao = db.taskDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun getAllActiveTasks_filtersOldHistory_keepsTodayHistory() = runBlocking {
        // --- 1. SETUP TIME ---
        // We pretend "Today" is January 1st, 2024
        val fixedDate = LocalDate.of(2026, 1, 1)
        val zone = ZoneId.systemDefault() // Use device zone to match app behavior

        // Calculate timestamps exactly like the App will
        val startOfToday = fixedDate.atStartOfDay(zone).toInstant().toEpochMilli()

        // "Yesterday" at Noon (Should be HIDDEN)
        val yesterdayNoon = fixedDate.minusDays(1).atTime(12, 0).atZone(zone).toInstant().toEpochMilli()

        // "Today" at Noon (Should be SHOWN)
        val todayNoon = fixedDate.atTime(12, 0).atZone(zone).toInstant().toEpochMilli()

        // --- 2. CREATE TASKS ---

        // Task A: Active (completedAt = null)
        dao.upsertTask(createTask("Active Task", null))

        // Task B: Done Today (completedAt = todayNoon)
        dao.upsertTask(createTask("Done Today", todayNoon))

        // Task C: Done Yesterday (completedAt = yesterdayNoon)
        dao.upsertTask(createTask("Done Yesterday", yesterdayNoon))

        // --- 3. ACT ---
        // We ask the DB: "Give me everything active or done since Jan 1st 00:00"
        val result = dao.getAllActiveTasks(startOfToday).first()

        // --- 4. ASSERT ---
        assertEquals("Should return exactly 2 tasks", 2, result.size)

        // Verify Content
        assertTrue("Contains Active Task", result.any { it.title == "Active Task" })
        assertTrue("Contains Done Today", result.any { it.title == "Done Today" })

        // Verify Exclusion
        assertTrue("Hides Done Yesterday", result.none { it.title == "Done Yesterday" })
    }
    // Helper to keep the test readable
    private fun createTask(title: String, completedAt: Long?): TaskEntity {
        return TaskEntity(
            id = UUID.randomUUID().toString(),
            title = title,
            completedAt = completedAt,
            // Defaults
            focusId = null, projectId = null, dueDate = null,
            difficulty = "EASY", priority = "LOW"
        )
    }
}