package com.example.watertracker.data

import com.example.watertracker.model.LogEntity

data class RemoteLog(
    val remoteId: String = "",
    val title: String = "",
    val tag: String = "",
    val note: String = "",
    val amountMl: Int = 0,
    val dateKey: String = "",
    val timeText: String = "",
    val updatedAt: Long = 0L,
    val deleted: Boolean = false
)

fun LogEntity.toRemote() = RemoteLog(
    remoteId, title, tag, note, amountMl, dateKey, timeText, updatedAt, deleted
)

fun RemoteLog.toEntity(id: Int) = LogEntity(
    id = id, title = title, tag = tag, note = note, amountMl = amountMl,
    dateKey = dateKey, timeText = timeText, remoteId = remoteId,
    updatedAt = updatedAt, synced = true, deleted = deleted
)