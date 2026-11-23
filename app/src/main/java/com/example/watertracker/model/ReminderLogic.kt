package com.example.watertracker.model

/** True when reminders must stay silent (default 22:00 to 07:00). */
fun isQuietHour(hour: Int, startHour: Int = 22, endHour: Int = 7): Boolean =
    hour >= startHour || hour < endHour
