package com.example.watertracker.ui.screens

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.watertracker.model.UserProfile
import com.example.watertracker.model.WaterUnit
import com.example.watertracker.ui.components.DividerLine
import com.example.watertracker.ui.components.GoalEditDialog
import com.example.watertracker.ui.components.LocationSettingsDialog
import com.example.watertracker.ui.components.QuietHoursDialog
import com.example.watertracker.ui.components.SettingsSectionHeader
import com.example.watertracker.ui.components.TopHeaderSection

private fun openSystemNotificationSettings(context: Context) {
    val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        }
    } else {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = "package:${context.packageName}".toUri()
        }
    }
    context.startActivity(intent)
}

/**
 * Settings and Preferences screen.
 *
 * Displays profile overview, daily goal configuration, unit system, responsive notification toggle,
 * quiet hours time selection, system notifications integration, and location settings.
 *
 * @param targetIntakeMl Active daily water target in milliliters.
 * @param unit Active measurement unit system.
 * @param userProfile Active user profile metrics.
 * @param onTargetGoalChange Callback triggered when daily water target is updated.
 * @param onUnitChange Callback triggered when unit system is switched.
 * @param onSaveProfile Callback triggered when user profile settings are updated.
 * @param onNavigateToProfileEdit Callback triggered when user taps the profile card to edit metrics.
 * @param onProfileClick Callback triggered when tapping the top profile avatar button.
 */
@Composable
fun SettingsScreen(
    targetIntakeMl: Int = 2850,
    unit: WaterUnit = WaterUnit.METRIC,
    userProfile: UserProfile = UserProfile(),
    onTargetGoalChange: (Int) -> Unit = {},
    onUnitChange: (WaterUnit) -> Unit = {},
    onSaveProfile: (UserProfile) -> Unit = {},
    onNavigateToProfileEdit: () -> Unit = {},
    onProfileClick: () -> Unit = {},
) {
    val context = LocalContext.current
    var showGoalDialog by remember { mutableStateOf(false) }
    var showUnitDialog by remember { mutableStateOf(false) }
    var showLocationDialog by remember { mutableStateOf(false) }
    var showQuietHoursDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        TopHeaderSection(onProfileClick = onProfileClick)

        // Title Block
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = "Settings",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
            )
            Text(
                text = "Manage your account and preferences",
                fontSize = 12.sp,
                color = Color(0xFF64748B),
            )
        }

        // Clickable Profile Card -> Navigates to ProfileEditScreen
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToProfileEdit() },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.weight(1f),
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0F2FE)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = Color(0xFF0284C7),
                            modifier = Modifier.size(30.dp),
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = userProfile.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                        )
                        Text(
                            text = "${userProfile.weightKg} kg • Age ${userProfile.age} • ${userProfile.activityLevel.label}",
                            fontSize = 12.sp,
                            color = Color(0xFF0284C7),
                            fontWeight = FontWeight.Medium,
                        )
                        Text(
                            text = "Location: ${userProfile.locationCity}",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B),
                        )
                    }
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Edit Profile",
                    tint = Color(0xFFCBD5E1),
                    modifier = Modifier.size(22.dp),
                )
            }
        }

        // Section 1: PREFERENCES
        SettingsSectionHeader(title = "PREFERENCES")
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        ) {
            Column {
                // Interactive Daily Water Goal Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showGoalDialog = true }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE0F2FE)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Default.WaterDrop,
                                contentDescription = "Goal",
                                tint = Color(0xFF0284C7),
                                modifier = Modifier.size(18.dp),
                            )
                        }
                        Column {
                            Text(
                                text = "Daily Water Goal",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF0F172A),
                            )
                            Text(
                                text = "Tap to modify target",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B),
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(
                            text = unit.formatVolume(targetIntakeMl),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0284C7),
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Edit Goal",
                            tint = Color(0xFFCBD5E1),
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }

                DividerLine()

                // Interactive Units System Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showUnitDialog = true }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE0F2FE)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Default.Straighten,
                                contentDescription = "Units",
                                tint = Color(0xFF0284C7),
                                modifier = Modifier.size(18.dp),
                            )
                        }
                        Column {
                            Text(
                                text = "Units",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF0F172A),
                            )
                            Text(
                                text = "Measurement unit system",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B),
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(
                            text = unit.label,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF64748B),
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Select Unit",
                            tint = Color(0xFFCBD5E1),
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }

                DividerLine()

                // Responsive Notifications Toggle Button Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE0F2FE)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Reminders",
                                tint = Color(0xFF0284C7),
                                modifier = Modifier.size(18.dp),
                            )
                        }
                        Column {
                            Text(
                                text = "Smart Reminders",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF0F172A),
                            )
                            Text(
                                text = if (userProfile.areNotificationsEnabled) "Responsive hydration alerts ON" else "Alerts turned OFF",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B),
                            )
                        }
                    }

                    Switch(
                        checked = userProfile.areNotificationsEnabled,
                        onCheckedChange = { isChecked ->
                            onSaveProfile(userProfile.copy(areNotificationsEnabled = isChecked))
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF0284C7),
                        ),
                    )
                }

                DividerLine()

                // Interactive Quiet Hours Selection Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showQuietHoursDialog = true }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEEF2FF)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bedtime,
                                contentDescription = "Quiet Hours",
                                tint = Color(0xFF4F46E5),
                                modifier = Modifier.size(18.dp),
                            )
                        }
                        Column {
                            Text(
                                text = "Quiet Hours",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF0F172A),
                            )
                            Text(
                                text = "Tap to configure quiet schedule",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B),
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(
                            text = if (userProfile.isQuietHoursEnabled) {
                                "${userProfile.quietHoursStart} – ${userProfile.quietHoursEnd}"
                            } else {
                                "Off"
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (userProfile.isQuietHoursEnabled) Color(0xFF4F46E5) else Color(0xFF64748B),
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Edit Quiet Hours",
                            tint = Color(0xFFCBD5E1),
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
            }
        }

        // Section 2: INTEGRATIONS & SYNC
        SettingsSectionHeader(title = "INTEGRATIONS & SYNC")
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFE4E6)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Health Connect",
                                tint = Color(0xFFE11D48),
                                modifier = Modifier.size(18.dp),
                            )
                        }
                        Text(
                            text = "Health Connect",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF0F172A),
                        )
                    }

                    Text(
                        text = "Connected",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF0D9488),
                    )
                }

                DividerLine()

                // Weather & Location Sync Card (Clickable to edit location settings)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showLocationDialog = true }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFEF3C7)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Default.WbSunny,
                                contentDescription = "Weather",
                                tint = Color(0xFFD97706),
                                modifier = Modifier.size(18.dp),
                            )
                        }
                        Column {
                            Text(
                                text = "Weather & Location Settings",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF0F172A),
                            )
                            Text(
                                text = "${userProfile.locationCity} • Tap to configure",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B),
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = if (userProfile.isLocationSyncEnabled) "Auto-on" else "Off",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (userProfile.isLocationSyncEnabled) Color(0xFF0284C7) else Color(0xFF64748B),
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Location Settings",
                            tint = Color(0xFFCBD5E1),
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
            }
        }

        // Section 3: ACCOUNT & SUPPORT
        SettingsSectionHeader(title = "ACCOUNT & SUPPORT")
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        ) {
            Column {
                // System Notifications Integration Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { openSystemNotificationSettings(context) }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEEF2FF)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "System Notifications",
                                tint = Color(0xFF4F46E5),
                                modifier = Modifier.size(18.dp),
                            )
                        }
                        Column {
                            Text(
                                text = "System Notification Settings",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF0F172A),
                            )
                            Text(
                                text = "Open device notification controls",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B),
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = "Configure",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF4F46E5),
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Open System Settings",
                            tint = Color(0xFFCBD5E1),
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }

                DividerLine()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF1F5F9)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Privacy",
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(18.dp),
                            )
                        }
                        Text(
                            text = "Privacy & Data",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF0F172A),
                        )
                    }
                }

                DividerLine()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEEF2FF)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.HelpOutline,
                                contentDescription = "Help",
                                tint = Color(0xFF4F46E5),
                                modifier = Modifier.size(18.dp),
                            )
                        }
                        Text(
                            text = "Help & FAQ",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF0F172A),
                        )
                    }
                }
            }
        }

        // Footer Version Note
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Quench v3.4 • Made with care",
                fontSize = 11.sp,
                color = Color(0xFF94A3B8),
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }

    if (showGoalDialog) {
        GoalEditDialog(
            currentTargetMl = targetIntakeMl,
            unit = unit,
            onDismiss = { showGoalDialog = false },
            onConfirm = { newGoalMl ->
                onTargetGoalChange(newGoalMl)
                showGoalDialog = false
            },
        )
    }

    if (showUnitDialog) {
        UnitSelectionDialog(
            currentUnit = unit,
            onDismiss = { showUnitDialog = false },
            onSelectUnit = { selectedUnit ->
                onUnitChange(selectedUnit)
                showUnitDialog = false
            },
        )
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

    if (showQuietHoursDialog) {
        QuietHoursDialog(
            initialEnabled = userProfile.isQuietHoursEnabled,
            initialStart = userProfile.quietHoursStart,
            initialEnd = userProfile.quietHoursEnd,
            onDismiss = { showQuietHoursDialog = false },
            onSaveQuietHours = { enabled, start, end ->
                onSaveProfile(
                    userProfile.copy(
                        isQuietHoursEnabled = enabled,
                        quietHoursStart = start,
                        quietHoursEnd = end,
                    )
                )
                showQuietHoursDialog = false
            },
        )
    }
}

/**
 * Dialog for selecting measurement unit system (Metric vs Imperial).
 */
@Composable
fun UnitSelectionDialog(
    currentUnit: WaterUnit,
    onDismiss: () -> Unit,
    onSelectUnit: (WaterUnit) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Select Unit System",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                WaterUnit.entries.forEach { option ->
                    val isSelected = option == currentUnit
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) Color(0xFFE0F2FE) else Color(0xFFF8FAFC))
                            .clickable { onSelectUnit(option) }
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = option.label,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color(0xFF0284C7) else Color(0xFF334155),
                            fontSize = 14.sp,
                        )
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = Color(0xFF0284C7),
                                modifier = Modifier.size(18.dp),
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = Color(0xFF64748B))
            }
        },
    )
}
