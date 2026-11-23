package com.example.watertracker.model

/** One day in the weekly chart. */
data class WeekDay(val label: String, val totalMl: Int, val isToday: Boolean)

/**
 * Days in a row the goal was reached. If today isn't finished yet,
 * counting starts from yesterday so the streak doesn't drop to 0 each morning.
 */
fun currentStreak(
    totals: Map<String, Int>,
    goalMl: Int,
    keyForDaysAgo: (Int) -> String = ::dateKeyDaysAgo
): Int {
    if (goalMl <= 0) return 0
    fun met(daysAgo: Int) = (totals[keyForDaysAgo(daysAgo)] ?: 0) >= goalMl

    var day = if (met(0)) 0 else 1
    var streak = 0
    while (met(day)) {
        streak++
        day++
    }
    return streak
}

/** The longest run of goal-reached days in the last [lookBackDays] days. */
fun longestStreak(
    totals: Map<String, Int>,
    goalMl: Int,
    lookBackDays: Int = 365,
    keyForDaysAgo: (Int) -> String = ::dateKeyDaysAgo
): Int {
    if (goalMl <= 0) return 0
    var best = 0
    var run = 0
    for (daysAgo in lookBackDays downTo 0) {
        if ((totals[keyForDaysAgo(daysAgo)] ?: 0) >= goalMl) {
            run++
            if (run > best) best = run
        } else {
            run = 0
        }
    }
    return best
}

/** The next streak milestone above the current streak (3, 7, 14, 30, 60, 100, then every 100). */
fun nextMilestone(streak: Int): Int =
    listOf(3, 7, 14, 30, 60, 100).firstOrNull { it > streak } ?: ((streak / 100) + 1) * 100

/** The last 7 days, oldest first, ending with today. */
fun lastSevenDays(
    totals: Map<String, Int>,
    keyForDaysAgo: (Int) -> String = ::dateKeyDaysAgo,
    labelForDaysAgo: (Int) -> String = ::dayLetterDaysAgo
): List<WeekDay> = (6 downTo 0).map { daysAgo ->
    WeekDay(labelForDaysAgo(daysAgo), totals[keyForDaysAgo(daysAgo)] ?: 0, daysAgo == 0)
}

/** Turns the week into the circles shown on the streak card. */
fun weekStatuses(week: List<WeekDay>, goalMl: Int): List<DayStatus> {
    val best = week.maxOfOrNull { it.totalMl } ?: 0
    return week.map { day ->
        val met = goalMl > 0 && day.totalMl >= goalMl
        val pct = if (goalMl > 0) day.totalMl * 100 / goalMl else 0
        DayStatus(
            label = day.label,
            pct = if (day.isToday && !met) "Today" else "$pct%",
            isCompleted = met,
            isPeak = met && day.totalMl == best,
            isToday = day.isToday
        )
    }
}