package com.example.watertracker.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.ui.graphics.Color

fun LogEntity.toLogEntry(): LogEntry = LogEntry(
    id = id,
    title = title,
    tag = tag,
    tagColor = Color(0xFFE0F2FE),
    tagTextColor = Color(0xFF0284C7),
    time = timeText,
    note = note,
    amountMl = amountMl,
    icon = Icons.Default.WaterDrop,
    iconBg = Color(0xFFE0F2FE),
    iconTint = Color(0xFF0284C7)
)