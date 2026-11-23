package com.example.watertracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.watertracker.model.WaterUnit
import com.example.watertracker.model.currentStreak
import com.example.watertracker.model.lastSevenDays
import com.example.watertracker.model.longestStreak
import com.example.watertracker.model.weekStatuses
import com.example.watertracker.ui.components.DailyIntakeBreakdownCard
import com.example.watertracker.ui.components.MonthlyIntakeProgressionCard
import com.example.watertracker.ui.components.StreakCard
import com.example.watertracker.ui.components.TopHeaderSection
import com.example.watertracker.ui.components.TrendsHeaderSection
import com.example.watertracker.ui.components.WeeklyIntakeProgressionCard

/**
 * Trends analytics screen: streak card plus Day / Week / Month charts.
 *
 * @param totals Map of date ("yyyy-MM-dd") to total ml drunk that day.
 */
@Composable
fun TrendsScreen(
    targetIntakeMl: Int = 2850,
    unit: WaterUnit = WaterUnit.METRIC,
    totals: Map<String, Int> = emptyMap(),
    onProfileClick: () -> Unit,
) {
    var selectedFilter by remember { mutableIntStateOf(1) } // 0: Day, 1: Week, 2: Month

    val week = remember(totals) { lastSevenDays(totals) }
    val streak = remember(totals, targetIntakeMl) { currentStreak(totals, targetIntakeMl) }
    val longest = remember(totals, targetIntakeMl) { longestStreak(totals, targetIntakeMl) }
    val statusDays = remember(week, targetIntakeMl) { weekStatuses(week, targetIntakeMl) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        TopHeaderSection(onProfileClick = onProfileClick)
        TrendsHeaderSection(
            selectedFilter = selectedFilter,
        ) { selectedFilter = it }
        StreakCard(streak = streak, longestStreak = longest, days = statusDays)

        when (selectedFilter) {
            0 -> DailyIntakeBreakdownCard(targetMl = targetIntakeMl, unit = unit)
            1 -> WeeklyIntakeProgressionCard(targetMl = targetIntakeMl, unit = unit, week = week)
            2 -> MonthlyIntakeProgressionCard(targetMl = targetIntakeMl, unit = unit)
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}