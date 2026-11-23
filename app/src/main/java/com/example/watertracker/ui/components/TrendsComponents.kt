package com.example.watertracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import com.example.watertracker.model.WeekDay
import com.example.watertracker.model.nextMilestone
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.watertracker.model.DayStatus
import com.example.watertracker.model.WaterUnit

/**
 * Top filter header section for the Trends screen.
 * Includes title, Live Sync status pill, and timeframe filter selector bar (Day, Week, Month).
 *
 * @param selectedFilter Active selected index (0: Day, 1: Week, 2: Month).
 * @param onFilterSelected Callback triggered when a timeframe tab is clicked.
 */
@Composable
fun TrendsHeaderSection(
    selectedFilter: Int = 1,
    onFilterSelected: (Int) -> Unit = {},
) {
    val filterOptions = listOf("Day", "Week", "Month")

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Hydration Trends",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFEEF2FF))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Sync,
                    contentDescription = "Sync",
                    tint = Color(0xFF4F46E5),
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "Live Sync",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF4F46E5)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFFEEF2FF))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            filterOptions.forEachIndexed { index, option ->
                val isSelected = index == selectedFilter
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isSelected) Color(0xFF0284C7) else Color.Transparent)
                        .clickable { onFilterSelected(index) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = option,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else Color(0xFF64748B)
                    )
                }
            }
        }
    }
}

/**
 * Card component displaying the current streak and the last 7 days.
 */
@Composable
fun StreakCard(
    streak: Int = 0,
    longestStreak: Int = 0,
    days: List<DayStatus> = emptyList()
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFEF3C7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Whatshot,
                        contentDescription = "Streak",
                        tint = Color(0xFFD97706),
                        modifier = Modifier.size(22.dp)
                    )
                }
                Column {
                    Text(
                        text = if (streak == 0) "No streak yet" else "$streak-Day Streak",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "Longest: $longestStreak days",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                days.forEach { day ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = day.label,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF64748B)
                        )
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        day.isPeak -> Color(0xFF0D9488)
                                        day.isCompleted -> Color(0xFF0284C7)
                                        else -> Color(0xFFE2E8F0)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (day.isPeak) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Peak",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            } else if (day.isCompleted) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Checked",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = "Not reached yet",
                                    tint = Color(0xFF64748B),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Text(
                            text = day.pct,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (day.isToday) Color(0xFF0284C7) else Color(0xFF64748B)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF0F9FF))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFEF3C7)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = "Milestone",
                                tint = Color(0xFFD97706),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "${nextMilestone(streak) - streak} days to ${nextMilestone(streak)}-Day Milestone!",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "Hit your goal every day to keep it going",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next",
                        tint = Color(0xFF0284C7),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

/**
 * Card component displaying daily (hourly) intake breakdown for the Day trend view.
 */
@Composable
fun DailyIntakeBreakdownCard(
    targetMl: Int = 2850,
    unit: WaterUnit = WaterUnit.METRIC
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "Daily Intake Distribution",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "Hourly breakdown throughout the day",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }
                Text(
                    text = unit.formatVolume(targetMl),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0284C7)
                )
            }

            val hourlyData = listOf(
                Pair("6 AM", 0.45f),
                Pair("9 AM", 0.85f),
                Pair("12 PM", 0.95f),
                Pair("3 PM", 0.70f),
                Pair("6 PM", 0.80f),
                Pair("9 PM", 0.50f)
            )

            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val targetY = size.height * 0.25f
                        drawLine(
                            color = Color(0xFFCBD5E1),
                            start = Offset(0f, targetY),
                            end = Offset(size.width, targetY),
                            strokeWidth = 2f,
                            pathEffect = PathEffect.dashPathEffect(
                                floatArrayOf(8f, 8f), 0f
                            )
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        hourlyData.forEachIndexed { idx, item ->
                            val isPeak = idx == 2
                            Box(
                                modifier = Modifier
                                    .width(28.dp)
                                    .fillMaxHeight(item.second)
                                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                    .background(
                                        if (isPeak) Color(0xFF0F766E) else Color(0xFF38BDF8)
                                    )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    hourlyData.forEach { item ->
                        Text(
                            text = item.first,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFE0F2FE))
                    .padding(10.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                        contentDescription = "Peak",
                        tint = Color(0xFF0284C7),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Peak Hydration Window: Midday (12 PM – 1 PM) with +${unit.fromMl(750)} ${unit.shortLabel} logged.",
                        fontSize = 11.sp,
                        color = Color(0xFF0369A1),
                        lineHeight = 15.sp
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SummaryStatBox(
                    modifier = Modifier.weight(1f),
                    label = "Morning Sip",
                    valStr = unit.fromMl(1250).toString(),
                    unitStr = unit.shortLabel,
                    badgeStr = "On Schedule",
                    badgeColor = Color(0xFF0284C7)
                )
                SummaryStatBox(
                    modifier = Modifier.weight(1f),
                    label = "Afternoon Sip",
                    valStr = unit.fromMl(1100).toString(),
                    unitStr = unit.shortLabel,
                    badgeStr = "Optimal Pace",
                    badgeColor = Color(0xFF10B981)
                )
                SummaryStatBox(
                    modifier = Modifier.weight(1f),
                    label = "Evening Goal",
                    valStr = unit.fromMl(500).toString(),
                    unitStr = unit.shortLabel,
                    badgeStr = "Light Sip",
                    badgeColor = Color(0xFFD97706)
                )
            }
        }
    }
}

/**
 * Card component displaying weekly intake progression bar chart.
 *
 * Automatically displays target baseline according to active target and [WaterUnit].
 *
 * @param targetMl Active daily intake target in milliliters.
 * @param unit Active measurement unit system.
 */
@Composable
fun WeeklyIntakeProgressionCard(
    targetMl: Int = 2850,
    unit: WaterUnit = WaterUnit.METRIC,
    week: List<WeekDay> = emptyList()
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "Weekly Intake Progression",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "Actual intake vs climate-adjusted goal",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }
                Text(
                    text = unit.formatVolume(targetMl),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0284C7)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF38BDF8))
                    )
                    Text("Intake (${unit.shortLabel})", fontSize = 11.sp, color = Color(0xFF64748B))
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0F766E))
                    )
                    Text("Heat Boost", fontSize = 11.sp, color = Color(0xFF64748B))
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(12.dp)
                            .height(2.dp)
                            .background(Color(0xFFCBD5E1))
                    )
                    Text("Dynamic Target", fontSize = 11.sp, color = Color(0xFF64748B))
                }
            }

            val safeTarget = targetMl.coerceAtLeast(1)
// the dashed target line sits 75% up the chart, so a day that hits the goal reaches it
            val heights = week.map { ((it.totalMl.toFloat() / safeTarget) * 0.75f).coerceIn(0.03f, 1f) }
            val dayLabels = week.map { it.label }
            val peakIdx = week.indices.maxByOrNull { week[it].totalMl } ?: -1

            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val targetY = size.height * 0.25f
                        drawLine(
                            color = Color(0xFFCBD5E1),
                            start = Offset(0f, targetY),
                            end = Offset(size.width, targetY),
                            strokeWidth = 2f,
                            pathEffect = PathEffect.dashPathEffect(
                                floatArrayOf(8f, 8f), 0f
                            )
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        heights.forEachIndexed { idx, heightPct ->
                            val isPeak = idx == peakIdx && week[idx].totalMl > 0
                            val isToday = week[idx].isToday
                            Box(
                                modifier = Modifier
                                    .width(26.dp)
                                    .fillMaxHeight(heightPct)
                                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                    .background(
                                        when {
                                            isPeak -> Color(0xFF0F766E)
                                            isToday -> Color(0xFFE0F2FE)
                                            else -> Color(0xFF38BDF8)
                                        }
                                    )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    dayLabels.forEachIndexed { idx, label ->
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            fontWeight = if (idx == peakIdx) FontWeight.Bold else FontWeight.Medium,
                            color = if (idx == peakIdx) Color(0xFF0F766E) else Color(0xFF64748B)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFE0F2FE))
                    .padding(10.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                        contentDescription = "Peak",
                        tint = Color(0xFF0284C7),
                        modifier = Modifier.size(18.dp)
                    )
                    val peakDay = week.getOrNull(peakIdx)
                    Text(
                        text = if (peakDay != null && peakDay.totalMl > 0) {
                            "Best day this week: ${peakDay.label} with ${unit.fromMl(peakDay.totalMl)} ${unit.shortLabel} logged."
                        } else {
                            "Log some water to see your weekly highlights."
                        },
                        fontSize = 11.sp,
                        color = Color(0xFF0369A1),
                        lineHeight = 15.sp
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val total = week.sumOf { it.totalMl }
                val daysMet = week.count { it.totalMl >= targetMl && targetMl > 0 }
                val bestDay = week.maxOfOrNull { it.totalMl } ?: 0

                SummaryStatBox(
                    modifier = Modifier.weight(1f),
                    label = "Daily Average",
                    valStr = "${unit.fromMl(total / 7)}",
                    unitStr = "${unit.shortLabel}/day",
                    badgeStr = "Last 7 days",
                    badgeColor = Color(0xFF0284C7)
                )
                SummaryStatBox(
                    modifier = Modifier.weight(1f),
                    label = "Target Met",
                    valStr = "${daysMet * 100 / 7}%",
                    unitStr = "$daysMet of 7 days",
                    badgeStr = if (daysMet >= 5) "Optimal Zone" else "Keep going",
                    badgeColor = Color(0xFF10B981)
                )
                SummaryStatBox(
                    modifier = Modifier.weight(1f),
                    label = "Best Day",
                    valStr = "${unit.fromMl(bestDay)}",
                    unitStr = unit.shortLabel,
                    badgeStr = "Peak",
                    badgeColor = Color(0xFFD97706)
                )
            }
        }
    }
}

/**
 * Card component displaying 4-week monthly intake progression bar chart.
 *
 * @param targetMl Active daily intake target in milliliters.
 * @param unit Active measurement unit system.
 */
@Composable
fun MonthlyIntakeProgressionCard(
    targetMl: Int = 2850,
    unit: WaterUnit = WaterUnit.METRIC
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "Monthly Intake Overview",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "4-week breakdown for this month",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }
                Text(
                    text = "${unit.fromMl(82500)} ${unit.shortLabel}/mo",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0284C7)
                )
            }

            val weeksData = listOf(
                Pair("W1", 0.78f),
                Pair("W2", 0.85f),
                Pair("W3", 0.72f),
                Pair("W4", 0.96f)
            )

            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val targetY = size.height * 0.25f
                        drawLine(
                            color = Color(0xFFCBD5E1),
                            start = Offset(0f, targetY),
                            end = Offset(size.width, targetY),
                            strokeWidth = 2f,
                            pathEffect = PathEffect.dashPathEffect(
                                floatArrayOf(8f, 8f), 0f
                            )
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        weeksData.forEachIndexed { idx, item ->
                            val isPeak = idx == 3
                            Box(
                                modifier = Modifier
                                    .width(42.dp)
                                    .fillMaxHeight(item.second)
                                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                    .background(
                                        if (isPeak) Color(0xFF0F766E) else Color(0xFF38BDF8)
                                    )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    weeksData.forEachIndexed { idx, _ ->
                        Text(
                            text = "Week ${idx + 1}",
                            fontSize = 11.sp,
                            fontWeight = if (idx == 3) FontWeight.Bold else FontWeight.Medium,
                            color = if (idx == 3) Color(0xFF0F766E) else Color(0xFF64748B)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFE0F2FE))
                    .padding(10.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                        contentDescription = "Peak",
                        tint = Color(0xFF0284C7),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Best Month Streak: Week 4 reached 105% of target goal with high consistency.",
                        fontSize = 11.sp,
                        color = Color(0xFF0369A1),
                        lineHeight = 15.sp
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SummaryStatBox(
                    modifier = Modifier.weight(1f),
                    label = "Monthly Total",
                    valStr = "${unit.fromMl(82500)}",
                    unitStr = unit.shortLabel,
                    badgeStr = "+8% vs last mo",
                    badgeColor = Color(0xFF0284C7)
                )
                SummaryStatBox(
                    modifier = Modifier.weight(1f),
                    label = "Consistency",
                    valStr = "92%",
                    unitStr = "26 of 28 days",
                    badgeStr = "High Score",
                    badgeColor = Color(0xFF10B981)
                )
                SummaryStatBox(
                    modifier = Modifier.weight(1f),
                    label = "Best Week",
                    valStr = "Week 4",
                    unitStr = "${unit.fromMl(21000)} ${unit.shortLabel}",
                    badgeStr = "Peak Goal",
                    badgeColor = Color(0xFFD97706)
                )
            }
        }
    }
}

/**
 * Summary metric callout box used inside progress analytics charts.
 */
@Composable
fun SummaryStatBox(
    modifier: Modifier = Modifier,
    label: String,
    valStr: String,
    unitStr: String,
    badgeStr: String,
    badgeColor: Color
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFF8FAFC))
            .padding(8.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(label, fontSize = 10.sp, color = Color(0xFF64748B))
            Text(valStr, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))
            Text(unitStr, fontSize = 10.sp, color = Color(0xFF64748B))
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = badgeStr,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = badgeColor
            )
        }
    }
}

/**
 * Card component displaying unlocked milestones, badges, and local community rank.
 */
@Composable
fun HydrationMilestonesCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Hydration Milestones",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "Achievements unlocked this cycle",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }

                Text(
                    text = "View All (12)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0284C7)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MilestoneBadgeItem(
                    modifier = Modifier.weight(1f),
                    title = "Heat Wave Hero",
                    sub = "3 days >30°C",
                    icon = Icons.Default.Whatshot,
                    iconBg = Color(0xFFFEF3C7),
                    iconTint = Color(0xFFD97706),
                    isUnlocked = true
                )
                MilestoneBadgeItem(
                    modifier = Modifier.weight(1f),
                    title = "Early Sips",
                    sub = "Pre-9am goal",
                    icon = Icons.Default.Schedule,
                    iconBg = Color(0xFFE0F2FE),
                    iconTint = Color(0xFF0284C7),
                    isUnlocked = true
                )
                MilestoneBadgeItem(
                    modifier = Modifier.weight(1f),
                    title = "Electrolyte Pro",
                    sub = "1 more session",
                    icon = Icons.Default.Bolt,
                    iconBg = Color(0xFFF1F5F9),
                    iconTint = Color(0xFF94A3B8),
                    isUnlocked = false
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFEEF2FF))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF4F46E5)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Groups,
                                contentDescription = "Rank",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = "Community Rank: Tier 1 Flow",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3730A3)
                        )
                    }

                    Text(
                        text = "Top 8% City",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4F46E5)
                    )
                }
            }
        }
    }
}

/**
 * Reusable badge item showing locked or unlocked milestone state.
 */
@Composable
fun MilestoneBadgeItem(
    modifier: Modifier = Modifier,
    title: String,
    sub: String,
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    isUnlocked: Boolean
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF8FAFC))
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = iconTint, modifier = Modifier.size(20.dp))
                if (isUnlocked) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .align(Alignment.BottomEnd)
                            .clip(CircleShape)
                            .background(Color(0xFF0284C7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Unlocked",
                            tint = Color.White,
                            modifier = Modifier.size(10.dp)
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .align(Alignment.BottomEnd)
                            .clip(CircleShape)
                            .background(Color(0xFF64748B)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Locked",
                            tint = Color.White,
                            modifier = Modifier.size(10.dp)
                        )
                    }
                }
            }
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                textAlign = TextAlign.Center
            )
            Text(
                text = sub,
                fontSize = 9.sp,
                color = Color(0xFF64748B),
                textAlign = TextAlign.Center
            )
        }
    }
}