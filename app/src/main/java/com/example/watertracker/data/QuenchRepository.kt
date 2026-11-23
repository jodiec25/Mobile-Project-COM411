package com.example.watertracker.data

import com.example.watertracker.model.DayTotal
import com.example.watertracker.model.LogDao
import com.example.watertracker.model.LogEntity
import com.example.watertracker.model.UserProfile
import com.example.watertracker.model.UserProfileDao
import com.example.watertracker.model.UserProfileEntity
import com.example.watertracker.model.toUserProfileEntity
import kotlinx.coroutines.flow.Flow

class QuenchRepository(
    private val logDao: LogDao,
    private val profileDao: UserProfileDao
) {
    fun logsForDate(dateKey: String): Flow<List<LogEntity>> = logDao.logsForDate(dateKey)

    fun dailyTotals(): Flow<List<DayTotal>> = logDao.dailyTotals()

    fun profile(): Flow<UserProfileEntity?> = profileDao.getUserProfile()

    suspend fun addLog(title: String, amountMl: Int, dateKey: String, timeText: String) {
        logDao.insert(
            LogEntity(
                title = title, tag = "Quick Log", note = "Manual entry",
                amountMl = amountMl, dateKey = dateKey, timeText = timeText
            )
        )
    }

    suspend fun editLog(id: Int, newAmountMl: Int) = logDao.updateAmount(id, newAmountMl)

    suspend fun deleteLog(id: Int) = logDao.deleteById(id)

    suspend fun saveProfile(profile: UserProfile) =
        profileDao.insertOrUpdateProfile(profile.toUserProfileEntity())

    /** Push local changes up, then pull newer changes down. Newest updatedAt wins. */
    suspend fun sync(remote: RemoteDataSource) {

        for (log in logDao.unsynced()) {
            remote.upsert(log.toRemote())
            logDao.markSynced(log.id, log.updatedAt)
        }

        for (r in remote.fetchAll()) {
            val local = logDao.findByRemoteId(r.remoteId)
            if (local == null) {
                if (!r.deleted) logDao.upsert(r.toEntity(id = 0))   // new log from another device
            } else if (r.updatedAt > local.updatedAt) {
                logDao.upsert(r.toEntity(id = local.id))             // cloud copy is newer: it wins
            }
        }
    }
}