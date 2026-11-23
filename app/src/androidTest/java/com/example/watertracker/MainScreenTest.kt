package com.example.watertracker

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class MainScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun tappingGlassAdds250() {
        composeRule.setContent { MainScreenTest() }
        composeRule.onNodeWithText("+250").performClick()
        composeRule.onNodeWithText("2,200").assertExists()
    }
}