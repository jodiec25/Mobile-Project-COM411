package com.example.watertracker

import org.junit.Assert.assertEquals
import org.junit.Test

class HydrationLogicTest { @Test
fun halfwayToGoal() {
    assertEquals(0.5f, progressFraction(1000, 2000), 0.001f)
}
    @Test
    fun goalOfZeroGivesZero() {
        assertEquals(0f, progressFraction(500, 0), 0.001f)
    }
}

