package com.example.watertracker.data

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.watertracker.model.AppDatabase
import java.util.concurrent.TimeUnit

class SyncWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        if (!SyncSettings.isEnabled(applicationContext)) return Result.success() // user hasn't opted in
        val remote = RemoteProvider.create() ?: return Result.success()
        return try {
            val db = AppDatabase.getInstance(applicationContext)
            QuenchRepository(db.logDao(), db.userProfileDao()).sync(remote)
            Result.success()
        } catch (e: Exception) {
            Result.retry()   // WorkManager tries again later with backoff
        }
    }

    companion object {
        // Only run when there is an internet connection
        private val needsInternet = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        fun syncNow(context: Context) {
            val request = OneTimeWorkRequestBuilder<SyncWorker>()
                .setConstraints(needsInternet)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
                .build()
            WorkManager.getInstance(context)
                .enqueueUniqueWork("quench_sync_now", ExistingWorkPolicy.REPLACE, request)
        }


        fun schedulePeriodic(context: Context) {
            val request = PeriodicWorkRequestBuilder<SyncWorker>(6, TimeUnit.HOURS)
                .setConstraints(needsInternet)
                .build()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "quench_sync_periodic", ExistingPeriodicWorkPolicy.KEEP, request
            )
        }
    }
}