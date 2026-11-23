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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.watertracker.model.LogEntry
import com.example.watertracker.model.UserProfile
import com.example.watertracker.model.WaterUnit
import com.example.watertracker.ui.components.HydrationGaugeCard
import com.example.watertracker.ui.components.LocationSettingsDialog
import com.example.watertracker.ui.components.LocationWeatherCard
import com.example.watertracker.ui.components.QuickLogSection
import com.example.watertracker.ui.components.TodayLogsSection
import com.example.watertracker.ui.components.TopHeaderSection

/**
 * Main dashboard screen for tracking today's water intake.
 *
 * Displays top header, circular hydration gauge, location & temperature details card,
 * quick log buttons, and today's log entries formatted in active [WaterUnit].
 *
 * @param currentIntakeMl Total intake volume recorded today in milliliters.
 * @param targetIntakeMl Daily intake target in milliliters.
 * @param unit Active measurement unit system.
 * @param userProfile Active user profile metrics.
 * @param temperatureC Active location temperature in degrees Celsius.
 * @param logs List of today's LogEntry records.
 * @param onAddWater Handler callback when quick log buttons are clicked.
 * @param onEditLog Handler callback when an existing log entry is edited.
 * @param onDeleteLog Handler callback when an existing log entry is deleted.
 * @param onSaveProfile Callback triggered when location or profile settings are updated.
 * @param onProfileClick Callback triggered when tapping the top profile avatar button.
 */
@Composable
fun TodayScreen(
    currentIntakeMl: Int,
    targetIntakeMl: Int,
    unit: WaterUnit = WaterUnit.METRIC,
    userProfile: UserProfile = UserProfile(),
    temperatureC: Double? = null,
    logs: List<LogEntry>,
    onAddWater: (Int, String) -> Unit,
    onEditLog: (LogEntry, Int) -> Unit = { _, _ -> },
    onDeleteLog: (LogEntry) -> Unit = {},
    onSaveProfile: (UserProfile) -> Unit = {},
    onProfileClick: () -> Unit,
) {
    var showLocationDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        TopHeaderSection(onProfileClick = onProfileClick)
        HydrationGaugeCard(
            currentMl = currentIntakeMl,
            targetMl = targetIntakeMl,
            unit = unit,
            userProfile = userProfile,
        )
        LocationWeatherCard(
            userProfile = userProfile,
            temperatureC = temperatureC,
            unit = unit,
        ) { showLocationDialog = true }
        QuickLogSection(unit = unit, onAddWater = onAddWater)
        TodayLogsSection(
            logs = logs,
            unit = unit,
            onEditLog = onEditLog,
            onDeleteLog = onDeleteLog,
        )
        Spacer(modifier = Modifier.height(12.dp))
    }

    if (showLocationDialog) {
        LocationSettingsDialog(
            currentCity = userProfile.locationCity,
            isSyncEnabled = userProfile.isLocationSyncEnabled,
            onDismiss = { showLocationDialog = false },
            onSaveLocationSettings = { newCity, newSync ->
                onSaveProfile(userProfile.copy(locationCity = newCity, isLocationSyncEnabled = newSync))
                showLocationDialog = false
            },
        )
    }
}
