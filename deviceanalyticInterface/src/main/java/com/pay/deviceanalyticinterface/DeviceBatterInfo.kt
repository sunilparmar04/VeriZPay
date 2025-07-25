package com.pay.deviceanalyticinterface

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeviceBatterInfo(val batterLevel: Int) : Parcelable
