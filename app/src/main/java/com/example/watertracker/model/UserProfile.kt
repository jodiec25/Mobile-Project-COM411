package com.example.watertracker.model

/**
 * Activity levels for user profile.
 */
enum class ActivityLevel(val label: String, val description: String) {
    LOW("Low", "Sedentary / Light daily activity"),
    MEDIUM("Medium", "Moderate daily exercise / active"),
    HIGH("High", "Heavy daily exercise / athlete")
}

/**
 * Data model for User Profile metrics.
 *
 * @property name User full name.
 * @property email User email address.
 * @property weightKg Weight in kilograms.
 * @property age Age in years.
 * @property activityLevel Activity level setting (LOW, MEDIUM, HIGH).
 * @property locationCity Active city name or "Auto (GPS)".
 * @property isLocationSyncEnabled Flag indicating whether weather/climate location sync is active.
 * @property areNotificationsEnabled Flag indicating whether push notifications are active.
 * @property isQuietHoursEnabled Flag indicating whether quiet hours are enabled.
 * @property quietHoursStart Start time string for quiet hours.
 * @property quietHoursEnd End time string for quiet hours.
 */
data class UserProfile(
    val name: String = "Quench User",
    val email: String = "user@quench.app",
    val weightKg: Int = 68,
    val age: Int = 28,
    val activityLevel: ActivityLevel = ActivityLevel.MEDIUM,
    val locationCity: String = "Auto (GPS)",
    val isLocationSyncEnabled: Boolean = true,
    val areNotificationsEnabled: Boolean = true,
    val isQuietHoursEnabled: Boolean = true,
    val quietHoursStart: String = "10:00 PM",
    val quietHoursEnd: String = "07:00 AM"
) {
    /**
     * Calculates recommended daily hydration target in milliliters based on body weight and activity level.
     */
    fun calculateRecommendedTargetMl(): Int {
        val baseMl = weightKg * 35
        val activityBoost = when (activityLevel) {
            ActivityLevel.LOW -> 0
            ActivityLevel.MEDIUM -> 470
            ActivityLevel.HIGH -> 950
        }
        return (baseMl + activityBoost).coerceIn(1500, 6000)
    }

    companion object {
        /**
         * Validates weight input in kilograms.
         */
        fun validateWeight(inputStr: String): String? {
            val trimmed = inputStr.trim()
            if (trimmed.isEmpty()) return "Weight cannot be empty."
            val weight = trimmed.toIntOrNull() ?: return "Please enter a valid whole number for weight."
            if (weight <= 0) return "Weight must be greater than zero."
            if (weight !in 20..300) return "Weight must be between 20 kg and 300 kg."
            return null
        }

        /**
         * Validates age input in years.
         */
        fun validateAge(inputStr: String): String? {
            val trimmed = inputStr.trim()
            if (trimmed.isEmpty()) return "Age cannot be empty."
            val ageVal = trimmed.toIntOrNull() ?: return "Please enter a valid whole number for age."
            if (ageVal <= 0) return "Age must be greater than zero."
            if (ageVal !in 1..120) return "Age must be between 1 and 120 years."
            return null
        }
    }
}
