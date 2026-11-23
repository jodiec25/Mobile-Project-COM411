package com.example.watertracker

import com.example.watertracker.model.ActivityLevel
import com.example.watertracker.model.UserProfile
import com.example.watertracker.model.toUserProfile
import com.example.watertracker.model.toUserProfileEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class UserProfileTest {

    @Test
    fun userProfileToEntityMapping() {
        val original = UserProfile(
            name = "Jane Doe",
            email = "jane@example.com",
            weightKg = 75,
            age = 30,
            activityLevel = ActivityLevel.HIGH
        )

        val entity = original.toUserProfileEntity()
        val mappedBack = entity.toUserProfile()

        assertEquals(original.name, mappedBack.name)
        assertEquals(original.email, mappedBack.email)
        assertEquals(original.weightKg, mappedBack.weightKg)
        assertEquals(original.age, mappedBack.age)
        assertEquals(original.activityLevel, mappedBack.activityLevel)
    }
}
