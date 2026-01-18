package com.raduleu.habithaven.core.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.raduleu.habithaven.core.data.local.HabitHavenDatabase
import com.raduleu.habithaven.core.data.local.dao.FocusDao
import com.raduleu.habithaven.core.data.local.dao.HabitDao
import com.raduleu.habithaven.core.data.local.entity.HabitEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class HabitDaoTest {

    private lateinit var db: HabitHavenDatabase
    private lateinit var dao: HabitDao
    private lateinit var focusDao: FocusDao


    @Before
    fun createDb() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, HabitHavenDatabase::class.java).build()
        dao = db.habitDao()
        focusDao = db.focusDao()

        val focus = com.raduleu.habithaven.core.data.local.entity.FocusEntity(
            id = "focus-1", // This ID matches what we use in createHabit
            name = "Health",
            sortOrder = 0,
            iconName = "heart",
            colorIndex = 0
        )
        focusDao.upsertFocus(focus)
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun getActiveHabits_filtersRetiredItems() = runBlocking {
        // 1. Create Active Habit
        val active = createHabit(title = "Gym", retired = false)
        dao.upsertHabit(active)

        // 2. Create Retired Habit (Old version)
        val retired = createHabit(title = "Old Gym", retired = true)
        dao.upsertHabit(retired)

        // 3. Act
        val result = dao.getActiveHabits().first()

        // 4. Assert
        assertEquals(1, result.size)
        assertEquals("Gym", result.first().title)
    }

    @Test
    fun getHabitChain_returnsAllVersions() = runBlocking {
        val CHAIN_ID = "chain-gym"

        // Version 1 (Oldest, Retired)
        val v1 = createHabit(title = "Gym V1", retired = true)
            .copy(chainId = CHAIN_ID, createdAt = 1000L)

        // Version 2 (Current, Active)
        val v2 = createHabit(title = "Gym V2", retired = false)
            .copy(chainId = CHAIN_ID, createdAt = 2000L)

        // Another habit (Different Chain)
        val other = createHabit(title = "Reading", retired = false)
            .copy(chainId = "chain-reading")

        dao.upsertHabit(v1)
        dao.upsertHabit(v2)
        dao.upsertHabit(other)

        // Act: Fetch chain for Gym
        val chain = dao.getHabitChain(CHAIN_ID).first()

        // Assert
        assertEquals("Should find 2 versions", 2, chain.size)
        assertTrue("Should contain V1", chain.any { it.title == "Gym V1" })
        assertTrue("Should contain V2", chain.any { it.title == "Gym V2" })
        assertTrue("Should NOT contain Reading", chain.none { it.title == "Reading" })
    }

    // Helper
    private fun createHabit(title: String, retired: Boolean): HabitEntity {
        return HabitEntity(
            id = UUID.randomUUID().toString(),
            chainId = UUID.randomUUID().toString(),
            focusId = "focus-1",
            title = title,
            retired = retired,
            repetitionRule = "{}", // Mock rule
            createdAt = System.currentTimeMillis(),
            // Defaults
            parentHabitId = null, projectId = null, difficulty = "EASY", priority = "LOW",
            isNewHabit = true, latestStreak = 0, bestStreak = 0, initialStreak = 0, skipped = 0, lastChecked = 0
        )
    }
}