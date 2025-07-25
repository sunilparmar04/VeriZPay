package com.pay.deviceanalytic.utils

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.util.Log
import com.pay.deviceanalyticinterface.DeviceBatterInfo
import com.pay.deviceanalyticinterface.DeviceCPUUsageInfo
import com.pay.deviceanalyticinterface.DeviceInfo

object DeviceUtils {

    fun getBatteryLevel(context: Context): DeviceBatterInfo {

        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            context.registerReceiver(null, ifilter)
        }

        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1

        Log.v("BatteryLevel", "Level:$level,scale:$scale")

        return DeviceBatterInfo(
            if (level > 0 && scale > 0)
                (level * 100) / scale
            else -1
        )

    }


    fun getDeviceInfo(): DeviceInfo {

        val isEmulator = (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.lowercase().contains("emulator")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.lowercase().contains("droid4x")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86"))

        return DeviceInfo(
            osVersion = Build.VERSION.RELEASE ?: "Unknown",
            apiLevel = Build.VERSION.SDK_INT,
            manufacturer = Build.MANUFACTURER ?: "Unknown",
            model = Build.MODEL ?: "Unknown",
            brand = Build.BRAND ?: "Unknown",
            device = Build.DEVICE ?: "Unknown",
            hardware = Build.HARDWARE ?: "Unknown",
            product = Build.PRODUCT ?: "Unknown",
            display = Build.DISPLAY ?: "Unknown",
            fingerprint = Build.FINGERPRINT ?: "Unknown",
            isEmulator = isEmulator
        )
    }


    fun getDeviceCPUUsageInfo(): DeviceCPUUsageInfo {
        return DeviceCPUUsageInfo(totalUsage = 50)
    }
}