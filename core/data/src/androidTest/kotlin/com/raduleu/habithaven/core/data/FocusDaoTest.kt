package com.raduleu.habithaven.core.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.raduleu.habithaven.core.data.local.HabitHavenDatabase
import com.raduleu.habithaven.core.data.local.dao.FocusDao
import com.raduleu.habithaven.core.data.local.dao.HabitDao
import com.raduleu.habithaven.core.data.local.dao.TaskDao
import com.raduleu.habithaven.core.data.local.entity.FocusEntity
import com.raduleu.habithaven.core.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FocusDaoTest {

    private lateinit var db: HabitHavenDatabase
    private lateinit var focusDao: FocusDao
    private lateinit var taskDao: TaskDao
    private lateinit var habitDao: HabitDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, HabitHavenDatabase::class.java).build()
        focusDao = db.focusDao()
        taskDao = db.taskDao()
        habitDao = db.habitDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun writeFocusAndReadInList() = runBlocking {
        val focus = FocusEntity(
            id = "focus-1",
            name = "Health",
            iconName = "heart",
            colorIndex = 1,
            sortOrder = 0
        )
        focusDao.upsertFocus(focus)

        val list = focusDao.getActiveFocuses().first()
        assertTrue(list.any { it.name == "Health" })
    }

    @Test
    fun getActiveFocusesWithChildren_handlesVariousChildCounts() = runBlocking {
        // --- SETUP ---
        val focus1 = FocusEntity(id = "f1", name = "Minimal", sortOrder = 1, iconName = "a", colorIndex = 1)
        val focus2 = FocusEntity(id = "f2", name = "Busy", sortOrder = 2, iconName = "b", colorIndex = 2)
        val focus3 = FocusEntity(id = "f3", name = "Empty", sortOrder = 3, iconName = "c", colorIndex = 3)

        focusDao.upsertFocus(focus1)
        focusDao.upsertFocus(focus2)
        focusDao.upsertFocus(focus3)

        taskDao.upsertTask(TaskEntity(id = "t1", focusId = "f1", title = "Task F1", difficulty = "EASY", priority = "LOW"))
        taskDao.upsertTask(TaskEntity(id = "t2a", focusId = "f2", title = "Task F2 A", difficulty = "EASY", priority = "LOW"))
        taskDao.upsertTask(TaskEntity(id = "t2b", focusId = "f2", title = "Task F2 B", difficulty = "EASY", priority = "LOW"))

        val habit = com.raduleu.habithaven.core.data.local.entity.HabitEntity(
            id = "h1", focusId = "f2", chainId = "c1", title = "Habit F2",
            repetitionRule = "{}", createdAt = 100L, difficulty = "EASY", priority = "LOW",
            retired = false, isNewHabit = true, latestStreak = 0, bestStreak = 0, initialStreak = 0, skipped = 0, lastChecked = 0
        )
         db.habitDao().upsertHabit(habit)

        // --- ACT ---
        val results = focusDao.getActiveFocusesWithChildren().first()

        // --- ASSERT ---
        assertEquals("Should return 3 focuses", 3, results.size)

        val f1Aggregate = results.find { it.focus.id == "f1" }!!
        assertEquals("Focus 1 should have 1 task", 1, f1Aggregate.tasks.size)
        assertEquals("Task should belong to F1", "Task F1", f1Aggregate.tasks[0].title)

        val f2Aggregate = results.find { it.focus.id == "f2" }!!
        assertEquals("Focus 2 should have 2 tasks", 2, f2Aggregate.tasks.size)

        val f3Aggregate = results.find { it.focus.id == "f3" }!!
        assertTrue("Focus 3 tasks should be empty list", f3Aggregate.tasks.isEmpty())
        assertTrue("Focus 3 habits should be empty list", f3Aggregate.habits.isEmpty())
    }

    @Test
    fun archiveFocus_hidesFromActiveList_butKeepsData() = runBlocking {
        // 1. Setup: Create Focus
        val focus = FocusEntity(id = "focus-arch", name = "Archive Me", isArchived = false, sortOrder = 1, colorIndex = 1, iconName = "x")
        focusDao.upsertFocus(focus)

        // 2. Act: Archive it
        focusDao.archiveFocus("focus-arch")

        // 3. Assert 1: Should NOT be in the active list
        val activeList = focusDao.getActiveFocuses().first()
        assertTrue("Archived focus should not be in active list", activeList.none { it.id == "focus-arch" })

        // 4. Assert 2: Should still exist in DB (lookup by ID)
        val retrieved = focusDao.getFocusById("focus-arch")
        assertEquals("Focus should still exist", "focus-arch", retrieved?.id)
        assertTrue("Focus should be marked archived", retrieved?.isArchived == true)
    }
}