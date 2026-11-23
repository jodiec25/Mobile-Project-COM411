package com.example.watertracker.data

interface RemoteDataSource {
    suspend fun upsert(log: RemoteLog)
    suspend fun fetchAll(): List<RemoteLog>
}

object RemoteProvider {
    fun create(): RemoteDataSource? = null
}