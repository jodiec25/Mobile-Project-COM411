package com.example.watertracker.model

import java.util.Locale
import kotlin.math.roundToInt

/**
 * Supported measurement units for water volume tracking.
 *
 * @property label Full display name for settings (e.g. "Metric (ml)", "Imperial (fl oz)").
 * @property shortLabel Abbreviated unit symbol for gauges and log entries (e.g. "ml", "fl oz").
 */
enum class WaterUnit(val label: String, val shortLabel: String) {
    METRIC("Metric (ml)", "ml"),
    IMPERIAL("Imperial (fl oz)", "fl oz");

    /**
     * Converts a volume in milliliters to the current unit representation.
     */
    fun fromMl(amountMl: Int): Int {
        return when (this) {
            METRIC -> amountMl
            IMPERIAL -> (amountMl / 29.5735f).roundToInt()
        }
    }

    /**
     * Converts a volume from the current unit back to milliliters.
     */
    fun toMl(amountInUnit: Int): Int {
        return when (this) {
            METRIC -> amountInUnit
            IMPERIAL -> (amountInUnit * 29.5735f).roundToInt()
        }
    }

    /**
     * Formats a volume in milliliters for display in the active unit system.
     */
    fun formatVolume(amountMl: Int): String {
        val converted = fromMl(amountMl)
        return String.format(Locale.getDefault(), "%,d %s", converted, shortLabel)
    }

    /**
     * Returns the valid minimum and maximum target goal limits for this unit system.
     */
    fun getGoalRange(): Pair<Int, Int> {
        return when (this) {
            METRIC -> Pair(500, 10000)      // 500 ml to 10,000 ml
            IMPERIAL -> Pair(17, 338)       // ~17 fl oz to 338 fl oz
        }
    }

    /**
     * Validates if an input goal value in this unit system is valid and realistic.
     * Returns an error message string if invalid, or null if valid.
     */
    fun validateGoalInput(inputStr: String): String? {
        val trimmed = inputStr.trim()
        if (trimmed.isEmpty()) {
            return "Goal cannot be empty."
        }
        val value = trimmed.toIntOrNull()
            ?: return "Please enter a valid whole number."
        if (value <= 0) {
            return "Daily goal must be greater than zero."
        }
        val (minVal, maxVal) = getGoalRange()
        if (value < minVal || value > maxVal) {
            return "Goal must be between $minVal $shortLabel and $maxVal $shortLabel."
        }
        return null
    }
}
