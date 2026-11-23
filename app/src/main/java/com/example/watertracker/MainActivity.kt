package com.example.watertracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.watertracker.data.SyncWorker
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.watertracker.model.LogEntry
import com.example.watertracker.model.UserProfile
import com.example.watertracker.model.WaterUnit
import com.example.watertracker.ui.components.QuenchBottomNavigation
import com.example.watertracker.ui.screens.ProfileEditScreen
import com.example.watertracker.ui.screens.SettingsScreen
import com.example.watertracker.ui.screens.SplashScreen
import com.example.watertracker.ui.screens.TargetScreen
import com.example.watertracker.ui.screens.TodayScreen
import com.example.watertracker.ui.screens.TrendsScreen
import com.example.watertracker.ui.theme.WaterTrackerTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.watertracker.model.toLogEntry
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import android.os.Build
import androidx.activity.viewModels
import com.example.watertracker.viewmodel.QuenchViewModel

/**
 * Main Activity entry point for the WaterTracker (Quench) application.
 */
class MainActivity : ComponentActivity() {

    private val viewModel: QuenchViewModel by viewModels()

    private val askNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= 33) {
            askNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        ReminderWorker.schedule(this)
        SyncWorker.schedulePeriodic(this)

        setContent {
            WaterTrackerTheme {
                QuenchApp(viewModel)
            }
        }
    }
}

@Composable
fun QuenchApp(viewModel: QuenchViewModel) {
    var showSplash by remember { mutableStateOf(true) }

    if (showSplash) {
        SplashScreen(onSplashFinished = { showSplash = false })
    } else {
        MainAppContent(viewModel)
    }
}
@Composable
fun MainAppContent(viewModel: QuenchViewModel) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedUnit by remember { mutableStateOf(WaterUnit.METRIC) }
    var isEditingProfile by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val entities by viewModel.todayLogs.collectAsState()
    val userProfile by viewModel.profile.collectAsState()
    val targetIntakeMl by viewModel.targetIntakeMl.collectAsState()
    val temperatureC by viewModel.temperatureC.collectAsState()
    val dailyTotals by viewModel.dailyTotals.collectAsState()

    val locationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) viewModel.loadWeather()
    }

    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (granted) viewModel.loadWeather()
        else locationLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    val logs: List<LogEntry> = entities.map { it.toLogEntry() }
    val currentIntakeMl: Int = entities.sumOf { it.amountMl }

    val onAddWater: (Int, String) -> Unit = { amountMl, label -> viewModel.addWater(amountMl, label) }
    val onEditLog: (LogEntry, Int) -> Unit = { entry, newAmountMl -> viewModel.editLog(entry.id, newAmountMl) }
    val onDeleteLog: (LogEntry) -> Unit = { entry -> viewModel.deleteLog(entry.id) }
    val navigateToSettingsTab = {
        selectedTab = 3
        isEditingProfile = false
    }

    val openProfileEditScreen = {
        selectedTab = 3
        isEditingProfile = true
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFF6F8FD),
        bottomBar = {
            QuenchBottomNavigation(
                selectedTab = selectedTab,
                onTabSelected = {
                    selectedTab = it
                    if (it != 3) isEditingProfile = false
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> TodayScreen(
                    currentIntakeMl = currentIntakeMl,
                    targetIntakeMl = targetIntakeMl,
                    unit = selectedUnit,
                    userProfile = userProfile,
                    temperatureC = temperatureC,
                    logs = logs,
                    onAddWater = onAddWater,
                    onEditLog = onEditLog,
                    onDeleteLog = onDeleteLog,
                    onSaveProfile = { updated -> viewModel.saveProfile(updated) },
                    onProfileClick = navigateToSettingsTab
                )
                1 -> TrendsScreen(
                    targetIntakeMl = targetIntakeMl,
                    unit = selectedUnit,
                    totals = dailyTotals,
                    onProfileClick = navigateToSettingsTab
                )
                2 -> TargetScreen(
                    currentIntakeMl = currentIntakeMl,
                    targetIntakeMl = targetIntakeMl,
                    unit = selectedUnit,
                    userProfile = userProfile,
                    onTargetGoalChange = { newGoalMl -> viewModel.setManualGoal(newGoalMl) },
                    onProfileClick = navigateToSettingsTab
                )
                3 -> {
                    if (isEditingProfile) {
                        ProfileEditScreen(
                            currentProfile = userProfile,
                            onSaveProfile = { updated -> viewModel.saveProfile(updated) },
                            onNavigateBack = {
                                isEditingProfile = false
                            }
                        )
                    } else {
                        SettingsScreen(
                            targetIntakeMl = targetIntakeMl,
                            unit = selectedUnit,
                            userProfile = userProfile,
                            onTargetGoalChange = { newGoalMl -> viewModel.setManualGoal(newGoalMl) },
                            onUnitChange = { newUnit -> selectedUnit = newUnit },
                            onSaveProfile = { updated -> viewModel.saveProfile(updated) },
                            onNavigateToProfileEdit = openProfileEditScreen,
                            onProfileClick = navigateToSettingsTab
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    WaterTrackerTheme {
        SplashScreen(onSplashFinished = {})
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileEditScreenPreview() {
    WaterTrackerTheme {
        ProfileEditScreen(
            currentProfile = UserProfile(),
            onSaveProfile = {},
            onNavigateBack = {}
        )
    }
}
