package com.example.watertracker.model

/**
 * Data model representing the hydration goal completion status for a specific day of the week.
 *
 * @property label Short day indicator label (e.g., "M", "T", "W").
 * @property pct Goal completion percentage string (e.g., "108%").
 * @property isCompleted Indicates whether the daily intake target was met.
 * @property isPeak Indicates whether this day represents a peak intake achievement.
 * @property isToday Indicates whether this entry represents the current calendar day.
 */
data class DayStatus(
    val label: String,
    val pct: String,
    val isCompleted: Boolean,
    val isPeak: Boolean,
    val isToday: Boolean
)
