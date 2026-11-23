package com.example.watertracker

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.watertracker.data.QuenchRepository
import com.example.watertracker.model.AppDatabase
import com.example.watertracker.model.UserProfile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QuenchRepositoryTest {

    private lateinit var db: AppDatabase
    private lateinit var repository: QuenchRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        repository = QuenchRepository(db.logDao(), db.userProfileDao())
    }

    @After
    fun tearDown() = db.close()

    @Test
    fun addedLogShowsUpForThatDay() = runBlocking<Unit> {
        repository.addLog("Quick Log", 250, "2026-11-21", "10:00 AM")

        val logs = repository.logsForDate("2026-11-21").first()

        assertEquals(1, logs.size)
        assertEquals(250, logs[0].amountMl)
    }

    @Test
    fun deletedLogIsGone() = runBlocking<Unit> {
        repository.addLog("Quick Log", 250, "2026-11-21", "10:00 AM")
        val id = repository.logsForDate("2026-11-21").first()[0].id

        repository.deleteLog(id)

        assertEquals(0, repository.logsForDate("2026-11-21").first().size)
    }

    @Test
    fun savedProfileIsReadBack() = runBlocking<Unit> {
        repository.saveProfile(UserProfile(name = "Jodie"))

        assertEquals("Jodie", repository.profile().first()?.name)
    }
}