package com.example.watertracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.watertracker.data.QuenchRepository
import com.example.watertracker.fetchTemperatureHere
import com.example.watertracker.model.AppDatabase
import com.example.watertracker.model.LogEntity
import com.example.watertracker.model.UserProfile
import com.example.watertracker.model.currentTimeText
import com.example.watertracker.model.todayKey
import com.example.watertracker.model.toUserProfile
import com.example.watertracker.model.weatherBonusMl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.watertracker.data.SyncWorker

class QuenchViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val repository = QuenchRepository(database.logDao(), database.userProfileDao())

    private val today = todayKey()
    private val manualGoalMl = MutableStateFlow<Int?>(null)   // set when the user types their own goal
    private val _temperatureC = MutableStateFlow<Double?>(null)
    val temperatureC: StateFlow<Double?> = _temperatureC

    val todayLogs: StateFlow<List<LogEntity>> = repository.logsForDate(today)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val profile: StateFlow<UserProfile> = repository.profile()
        .map { it?.toUserProfile() ?: UserProfile() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UserProfile())

    /** Goal = the user's own goal if they set one, else profile goal + weather bonus. */
    val targetIntakeMl: StateFlow<Int> =
        combine(profile, _temperatureC, manualGoalMl) { p, temp, manual ->
            manual ?: (p.calculateRecommendedTargetMl() + weatherBonusMl(temp))
        }.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5_000),
            UserProfile().calculateRecommendedTargetMl()
        )
    /** date → total ml, for streaks and charts */
    val dailyTotals: StateFlow<Map<String, Int>> = repository.dailyTotals()
        .map { list -> list.associate { it.dateKey to it.totalMl } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyMap())

    fun addWater(amountMl: Int, label: String) {
        viewModelScope.launch {
            repository.addLog(label, amountMl, today, currentTimeText())
            SyncWorker.syncNow(getApplication())
        }
    }

    fun editLog(id: Int, newAmountMl: Int) {
        viewModelScope.launch {
            repository.editLog(id, newAmountMl)
            SyncWorker.syncNow(getApplication())
        }
    }

    fun deleteLog(id: Int) {
        viewModelScope.launch {
            repository.deleteLog(id)
            SyncWorker.syncNow(getApplication())
        }
    }

    fun saveProfile(updated: UserProfile) {
        viewModelScope.launch { repository.saveProfile(updated) }
        manualGoalMl.value = null            // a new profile means a freshly calculated goal
    }

    fun setManualGoal(goalMl: Int) {
        manualGoalMl.value = goalMl
    }

    fun loadWeather() {
        viewModelScope.launch { _temperatureC.value = fetchTemperatureHere(getApplication()) }
    }
}