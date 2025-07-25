package com.pay.verizpay.ui.paymentscreen

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pay.deviceanalyticinterface.AnalyticService
import com.pay.deviceanalyticinterface.DeviceAnalytics
import com.pay.deviceanalyticinterface.periodicsync.DeviceHealthWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val analyticsService: AnalyticService
) : ViewModel() {

    var amount = mutableStateOf("")
        private set

    fun onAmountChange(newAmount: String) {
        amount.value = newAmount
    }

    private val _deviceInfo = MutableStateFlow<DeviceAnalytics?>(null)
    val deviceInfo: StateFlow<DeviceAnalytics?> = _deviceInfo

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun initiatePayment() {
        viewModelScope.launch {
            _isLoading.value = true
            val deviceInfo = analyticsService.getAnalyticsInterface()?.getDeviceInfo()

            delay(5000L) // Added just to show progress
            _deviceInfo.value = deviceInfo
            _isLoading.value = false
            Log.v(
                "deviceInfo",
                "From AIDL deviceInfo:${deviceInfo?.batteryInfo?.batterLevel} \n deviceDetails:${deviceInfo?.deviceInfo}"
            )
        }
    }

    fun initWorker(context: Context) {
        DeviceHealthWorker.schedule(context =context )

    }
}
