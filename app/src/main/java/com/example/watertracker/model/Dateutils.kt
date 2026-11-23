package com.example.watertracker.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Calendar

/** Used to group drinks by day */
fun todayKey(): String =
    SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

/** The current time as shown in the logs list */
fun currentTimeText(): String =
    SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())

/** The date key for N days ago. 0 = today, 1 = yesterday. */
fun dateKeyDaysAgo(daysAgo: Int): String {
    val cal = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_YEAR, -daysAgo)
    return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(cal.time)
}

/** First letter of the weekday N days ago, e.g. "M". */
fun dayLetterDaysAgo(daysAgo: Int): String {
    val cal = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_YEAR, -daysAgo)
    return SimpleDateFormat("EEE", Locale.ENGLISH).format(cal.time).take(1).uppercase()
}