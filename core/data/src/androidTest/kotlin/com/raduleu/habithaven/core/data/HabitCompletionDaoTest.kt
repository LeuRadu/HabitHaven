package com.raduleu.habithaven.core.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.raduleu.habithaven.core.data.local.HabitHavenDatabase
import com.raduleu.habithaven.core.data.local.dao.HabitCompletionDao
import com.raduleu.habithaven.core.data.local.entity.HabitCompletionEntity
import com.raduleu.habithaven.core.data.local.entity.HabitEntity // Needed for Foreign Key
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
class HabitCompletionDaoTest {

    private lateinit var db: HabitHavenDatabase
    private lateinit var dao: HabitCompletionDao
    private val CHAIN_ID = "chain-running"

    @Before
    fun createDb() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, HabitHavenDatabase::class.java).build()
        dao = db.habitCompletionDao()

        // 1. Create FOCUS (The Grandparent)
        val focusDao = db.focusDao()
        focusDao.upsertFocus(
            com.raduleu.habithaven.core.data.local.entity.FocusEntity(
                id = "focus-1",
                name = "Health",
                sortOrder = 0, iconName = "h", colorIndex = 0
            )
        )

        // 2. Create HABIT (The Parent)
        val habitDao = db.habitDao()
        val habit = HabitEntity(
            id = "habit-v1",
            chainId = CHAIN_ID,
            focusId = "focus-1", // Links to above
            title = "Running",
            retired = false,
            repetitionRule = "{}",
            createdAt = System.currentTimeMillis(),
            parentHabitId = null, projectId = null, difficulty = "EASY", priority = "LOW",
            isNewHabit = true, latestStreak = 0, bestStreak = 0, initialStreak = 0, skipped = 0, lastChecked = 0
        )
        habitDao.upsertHabit(habit)
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun getCompletionsForChain_retrievesAllHistoryForConcept() = runBlocking {
        // 1. Insert 3 completions for "Running" (The default chain)
        val c1 = createCompletion("habit-v1", CHAIN_ID, 1000L)
        val c2 = createCompletion("habit-v1", CHAIN_ID, 2000L)
        val c3 = createCompletion("habit-v1", CHAIN_ID, 3000L)

        dao.insertCompletion(c1)
        dao.insertCompletion(c2)
        dao.insertCompletion(c3)

        // 2. Insert 1 completion for "Reading" (A DIFFERENT Chain)
        val readingHabit = HabitEntity(
            id = "h2",
            chainId = "chain-read", // Different chain ID
            focusId = "focus-1",
            title = "Read",
            retired = false,
            repetitionRule = "{}",
            createdAt = 0,
            parentHabitId = null, projectId = null, difficulty = "EASY", priority = "LOW",
            isNewHabit = true, latestStreak = 0, bestStreak = 0, initialStreak = 0, skipped = 0, lastChecked = 0
        )
        db.habitDao().upsertHabit(readingHabit)

        // Insert completion for the DIFFERENT chain
        dao.insertCompletion(createCompletion("h2", "chain-read", 4000L))

        // 3. Act: Fetch history ONLY for "Running"
        val results = dao.getCompletionsForHabitChain(CHAIN_ID).first()

        // 4. Assert
        assertEquals("Should find exactly 3 completions (ignoring the reading one)", 3, results.size)
        assertTrue("All items should match the requested chain", results.all { it.chainId == CHAIN_ID })
    }

    @Test
    fun getCompletionsBetween_filtersByDateRange() = runBlocking {
        val zone = ZoneId.systemDefault()

        // Dates: Jan 1st, Jan 15th, Jan 31st
        val jan1 = LocalDate.of(2024, 1, 1).atStartOfDay(zone).toInstant().toEpochMilli()
        val jan15 = LocalDate.of(2024, 1, 15).atStartOfDay(zone).toInstant().toEpochMilli()
        val jan31 = LocalDate.of(2024, 1, 31).atStartOfDay(zone).toInstant().toEpochMilli()

        dao.insertCompletion(createCompletion("habit-v1", CHAIN_ID, jan1))
        dao.insertCompletion(createCompletion("habit-v1", CHAIN_ID, jan15))
        dao.insertCompletion(createCompletion("habit-v1", CHAIN_ID, jan31))

        // Act: Query range Jan 10 -> Jan 20 ... Should only find Jan 15
        val start = LocalDate.of(2024, 1, 10).atStartOfDay(zone).toInstant().toEpochMilli()
        val end = LocalDate.of(2024, 1, 20).atStartOfDay(zone).toInstant().toEpochMilli()

        val results = dao.getCompletionsBetween(start, end).first()

        // Assert
        assertEquals("Should find exactly 1 completion", 1, results.size)
        assertEquals("Should be the Jan 15th one", jan15, results.first().timestamp)
    }

    private fun createCompletion(habitId: String, chainId: String, timestamp: Long): HabitCompletionEntity {
        return HabitCompletionEntity(
            id = UUID.randomUUID().toString(),
            habitId = habitId,
            chainId = chainId,
            timestamp = timestamp
        )
    }
}