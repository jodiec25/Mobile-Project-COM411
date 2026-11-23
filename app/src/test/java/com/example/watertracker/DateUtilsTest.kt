package com.example.watertracker
import com.example.watertracker.model.todayKey
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DateUtilsTest {

    @Test
    fun todayKeyHasYearMonthDayFormat() {
        // e.g. "2026-11-08": 4 digits, dash, 2 digits, dash, 2 digits
        assertTrue(todayKey().matches(Regex("\\d{4}-\\d{2}-\\d{2}")))
    }

    @Test
    fun todayKeyIsTheSameWhenCalledTwiceInARow() {
        assertEquals(todayKey(), todayKey())
    }
}