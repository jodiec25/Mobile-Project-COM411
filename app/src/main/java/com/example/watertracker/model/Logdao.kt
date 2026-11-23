package com.example.watertracker.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LogDao {

    @Insert
    suspend fun insert(entry: LogEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entry: LogEntity)

    @Query("SELECT * FROM log_entries WHERE dateKey = :dateKey AND deleted = 0 ORDER BY id DESC")
    fun logsForDate(dateKey: String): Flow<List<LogEntity>>

    @Query(
        "UPDATE log_entries SET amountMl = :amountMl, synced = 0, " +
                "updatedAt = CAST(strftime('%s','now') AS INTEGER) * 1000 WHERE id = :id"
    )
    suspend fun updateAmount(id: Int, amountMl: Int)

    // Soft delete: the row is hidden but kept until delete has been synced
    @Query(
        "UPDATE log_entries SET deleted = 1, synced = 0, " +
                "updatedAt = CAST(strftime('%s','now') AS INTEGER) * 1000 WHERE id = :id"
    )
    suspend fun deleteById(id: Int)

    // --- used by sync ---
    @Query("SELECT * FROM log_entries WHERE synced = 0")
    suspend fun unsynced(): List<LogEntity>

    // Only marks the row synced if it hasn't been edited again since we uploaded it
    @Query("UPDATE log_entries SET synced = 1 WHERE id = :id AND updatedAt = :updatedAt")
    suspend fun markSynced(id: Int, updatedAt: Long)

    @Query("SELECT * FROM log_entries WHERE remoteId = :remoteId LIMIT 1")
    suspend fun findByRemoteId(remoteId: String): LogEntity?

    @Query("SELECT dateKey, SUM(amountMl) AS totalMl FROM log_entries WHERE deleted = 0 GROUP BY dateKey")
    fun dailyTotals(): Flow<List<DayTotal>>
}
