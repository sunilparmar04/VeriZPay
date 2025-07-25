package com.pay.deviceanalyticinterface

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeviceInfo(
    val osVersion: String,
    val apiLevel: Int,
    val manufacturer: String,
    val model: String,
    val brand: String,
    val device: String,
    val hardware: String,
    val product: String,
    val display: String,
    val fingerprint: String,
    val isEmulator: Boolean
) : Parcelable
