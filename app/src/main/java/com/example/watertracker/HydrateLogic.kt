package com.example.watertracker

fun progressFraction(totalMl: Int, goalMl: Int): Float {
    if (goalMl <= 0) return 0f
    return (totalMl.toFloat() / goalMl).coerceIn(0f, 1f)
}
