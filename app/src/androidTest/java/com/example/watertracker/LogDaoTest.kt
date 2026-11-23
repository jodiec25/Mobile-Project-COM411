package com.example.watertracker
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.watertracker.model.AppDatabase
import com.example.watertracker.model.LogDao
import com.example.watertracker.model.LogEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LogDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: LogDao

    private val day = "2026-11-08"

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        dao = db.logDao()
    }

    @After
    fun closeDatabase() {
        db.close()
    }

    private fun entry(amountMl: Int, dateKey: String = day) = LogEntity(
        title = "Quick Log",
        tag = "Quick Log",
        note = "Manual entry",
        amountMl = amountMl,
        dateKey = dateKey,
        timeText = "10:00 AM"
    )

    @Test
    fun savedLogCanBeReadBack() = runBlocking<Unit> {
        dao.insert(entry(250))

        val logs = dao.logsForDate(day).first()

        assertEquals(1, logs.size)
        assertEquals(250, logs[0].amountMl)
    }

    @Test
    fun totalOfSeveralLogsAddsUp() = runBlocking<Unit> {
        dao.insert(entry(250))
        dao.insert(entry(500))
        dao.insert(entry(750))

        val total = dao.logsForDate(day).first().sumOf { it.amountMl }

        assertEquals(1500, total)   // 250 + 500 + 750
    }

    @Test
    fun logsFromOtherDaysAreNotCounted() = runBlocking<Unit> {
        dao.insert(entry(250, dateKey = day))
        dao.insert(entry(500, dateKey = "2026-11-07"))

        val logs = dao.logsForDate(day).first()

        assertEquals(1, logs.size)
        assertEquals(250, logs[0].amountMl)
    }

    @Test
    fun editingALogChangesItsAmount() = runBlocking<Unit> {
        dao.insert(entry(250))
        val saved = dao.logsForDate(day).first()[0]

        dao.updateAmount(saved.id, 400)

        assertEquals(400, dao.logsForDate(day).first()[0].amountMl)
    }

    @Test
    fun deletingALogRemovesIt() = runBlocking<Unit> {
        dao.insert(entry(250))
        val saved = dao.logsForDate(day).first()[0]

        dao.deleteById(saved.id)

        assertTrue(dao.logsForDate(day).first().isEmpty())
    }
}