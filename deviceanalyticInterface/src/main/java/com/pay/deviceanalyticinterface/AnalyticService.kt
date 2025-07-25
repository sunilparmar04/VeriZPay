package com.pay.deviceanalyticinterface

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine

import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class AnalyticService @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private var deviceAnalytic: IDeviceAnalytics? = null


    suspend fun getAnalyticsInterface(): IDeviceAnalytics? {
        if (deviceAnalytic != null) {
            return deviceAnalytic
        }

        return createAnalyticInterface()
    }


    private suspend fun createAnalyticInterface(): IDeviceAnalytics? =
        suspendCancellableCoroutine { continuation ->

            val serviceConnection = object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

                    deviceAnalytic = IDeviceAnalytics.Stub.asInterface(service)
                    if (continuation.isActive)
                        continuation.resume(deviceAnalytic)
                }

                override fun onServiceDisconnected(name: ComponentName?) {
                    deviceAnalytic = null
                    continuation.resume(null)
                }
            }
            val serviceBindingStatus = context.bindService(
                Intent().apply {
                    component = ComponentName(
                        PACKAGE_ANALYTICS_SERVICE,
                        ANALYTICS_SERVICE
                    )
                    action = ANALYTICS_SERVICE_BINDER_ACTION
                },
                serviceConnection,
                Context.BIND_AUTO_CREATE
            )

            if (!serviceBindingStatus) {
                if (continuation.isActive) continuation.resume(null)
            }

        }


    companion object {
        private const val TAG = "AnalyticsService"

        private const val PACKAGE_ANALYTICS_SERVICE = "com.pay.deviceanalytic"
        private const val ANALYTICS_SERVICE = "com.pay.deviceanalytic.service.DeviceAnalyticService"
        private const val ANALYTICS_SERVICE_BINDER_ACTION =
            "com.pay.deviceanalytic.BIND_ANALYTICS_SERVICE"
    }
}


