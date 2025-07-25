package com.pay.verizpay

import android.app.Application

import com.pay.deviceanalyticinterface.periodicsync.DeviceHealthWorker
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class VeriZPayApp : Application() {

    override fun onCreate() {
        super.onCreate()

        DeviceHealthWorker.schedule(this)
    }

}