package com.pay.deviceanalytic.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.pay.deviceanalytic.utils.DeviceUtils
import com.pay.deviceanalyticinterface.DeviceAnalytics
import com.pay.deviceanalyticinterface.IDeviceAnalytics


class DeviceAnalyticService : Service() {

    private val deviceAnalyticsInterface by lazy {
        object : IDeviceAnalytics.Stub() {
            override fun getDeviceInfo(): DeviceAnalytics {
                return DeviceAnalytics(
                    batteryInfo = DeviceUtils.getBatteryLevel(this@DeviceAnalyticService),
                    deviceInfo = DeviceUtils.getDeviceInfo(),
                    deviceCPUUsageInfo = DeviceUtils.getDeviceCPUUsageInfo()
                )
            }
        }
    }

    override fun onBind(intent: Intent): IBinder? {

        val binder = deviceAnalyticsInterface.asBinder()
        Log.d(TAG, "Service onBind")

        binder.linkToDeath(
            {
                Log.d(TAG, "Service Killed")
            }, 0
        )
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "Service onUnbind")

        return super.onUnbind(intent)
    }

    companion object {
        val TAG: String = DeviceAnalyticService::class.java.simpleName
    }


}