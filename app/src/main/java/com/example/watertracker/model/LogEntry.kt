package com.example.watertracker.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Data model representing an individual water intake log record.
 *
 * @property id Unique identifier for the log entry.
 * @property title Short descriptive title for the intake (e.g., "Midday Refresher").
 * @property tag Category or type tag (e.g., "Infused Lemon", "Pure Water").
 * @property tagColor Background color for the category tag badge.
 * @property tagTextColor Text color for the category tag badge.
 * @property time Time string when the intake was recorded (e.g., "03:40 PM").
 * @property note Context note for the intake (e.g., "Desk refresh").
 * @property amountMl Water volume logged in milliliters.
 * @property icon Vector icon associated with the beverage container type.
 * @property iconBg Background color for the icon container circle.
 * @property iconTint Color tint applied to the vector icon.
 */
data class LogEntry(
    val id: Int,
    val title: String,
    val tag: String,
    val tagColor: Color,
    val tagTextColor: Color,
    val time: String,
    val note: String,
    val amountMl: Int,
    val icon: ImageVector,
    val iconBg: Color,
    val iconTint: Color
)
