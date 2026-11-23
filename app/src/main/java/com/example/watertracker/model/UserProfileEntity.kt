package com.example.watertracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val email: String,
    val weightKg: Int,
    val age: Int,
    val activityLevelName: String,
    val locationCity: String = "Auto (GPS)",
    val isLocationSyncEnabled: Boolean = true,
    val areNotificationsEnabled: Boolean = true,
    val isQuietHoursEnabled: Boolean = true,
    val quietHoursStart: String = "10:00 PM",
    val quietHoursEnd: String = "07:00 AM"
)

fun UserProfileEntity.toUserProfile(): UserProfile {
    val level = try {
        ActivityLevel.valueOf(activityLevelName)
    } catch (_: Exception) {
        ActivityLevel.MEDIUM
    }
    return UserProfile(
        name = name,
        email = email,
        weightKg = weightKg,
        age = age,
        activityLevel = level,
        locationCity = locationCity,
        isLocationSyncEnabled = isLocationSyncEnabled,
        areNotificationsEnabled = areNotificationsEnabled,
        isQuietHoursEnabled = isQuietHoursEnabled,
        quietHoursStart = quietHoursStart,
        quietHoursEnd = quietHoursEnd
    )
}

fun UserProfile.toUserProfileEntity(id: Int = 1): UserProfileEntity {
    return UserProfileEntity(
        id = id,
        name = name,
        email = email,
        weightKg = weightKg,
        age = age,
        activityLevelName = activityLevel.name,
        locationCity = locationCity,
        isLocationSyncEnabled = isLocationSyncEnabled,
        areNotificationsEnabled = areNotificationsEnabled,
        isQuietHoursEnabled = isQuietHoursEnabled,
        quietHoursStart = quietHoursStart,
        quietHoursEnd = quietHoursEnd
    )
}
