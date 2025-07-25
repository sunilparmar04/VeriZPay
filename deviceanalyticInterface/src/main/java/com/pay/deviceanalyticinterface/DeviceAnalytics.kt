package com.pay.deviceanalyticinterface

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeviceAnalytics(val batteryInfo: DeviceBatterInfo, val deviceInfo: DeviceInfo,val deviceCPUUsageInfo: DeviceCPUUsageInfo) :
    Parcelable
