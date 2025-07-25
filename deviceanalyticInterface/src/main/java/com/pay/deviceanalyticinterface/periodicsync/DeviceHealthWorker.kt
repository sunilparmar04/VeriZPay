package com.pay.deviceanalyticinterface.periodicsync

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.pay.deviceanalyticinterface.AnalyticService
import com.pay.deviceanalyticinterface.DeviceAnalytics
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class DeviceHealthWorker @AssistedInject constructor(
    private val analyticsService: AnalyticService,
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val deviceInfo = fetchDeviceInfo()
        Log.i(TAG, "Fetched Device Info: ${deviceInfo?.deviceInfo?.model}")
        return Result.success()
    }

    private suspend fun fetchDeviceInfo(): DeviceAnalytics? {
        return analyticsService.getAnalyticsInterface()?.deviceInfo
    }

    companion object {
        private const val TAG = "DeviceHealthWorker"

        fun schedule(context: Context) {

            val request = PeriodicWorkRequestBuilder<DeviceHealthWorker>(
                repeatInterval = 20,
                repeatIntervalTimeUnit = TimeUnit.MINUTES,
                flexTimeInterval = 5,
                flexTimeIntervalUnit = TimeUnit.MINUTES
            ).addTag(TAG).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                TAG,
                ExistingPeriodicWorkPolicy.UPDATE,
                request
            )
        }
    }
}
