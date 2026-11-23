package com.example.watertracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "log_entries")
data class LogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val tag: String,
    val note: String,
    val amountMl: Int,
    val dateKey: String,
    val timeText: String,
    // sync fields
    val remoteId: String = UUID.randomUUID().toString(),
    val updatedAt: Long = System.currentTimeMillis(),
    val synced: Boolean = false,
    val deleted: Boolean = false
)