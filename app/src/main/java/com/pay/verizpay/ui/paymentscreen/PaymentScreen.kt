package com.pay.verizpay.ui.paymentscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pay.deviceanalyticinterface.DeviceBatterInfo
import com.pay.deviceanalyticinterface.DeviceCPUUsageInfo
import com.pay.deviceanalyticinterface.DeviceInfo
import com.pay.verizpay.components.PaymentLoader


@Composable
fun PaymentScreen(modifier: Modifier = Modifier) {
    val viewModel: PaymentViewModel = hiltViewModel()
    val deviceInfo by viewModel.deviceInfo.collectAsState()
    val amount by viewModel.amount
    val isLoading by viewModel.isLoading.collectAsState()

    viewModel.initWorker(LocalContext.current)
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9F7FF))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
        ) {
            Text(
                text = "Payment Info",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                ),
                textAlign = TextAlign.Center
            )

            OutlinedTextField(
                value = amount,
                onValueChange = { viewModel.onAmountChange(it) },
                label = { Text("Enter Amount") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            deviceInfo?.let {
                PaymentContent(it.batteryInfo, it.deviceInfo, it.deviceCPUUsageInfo, amount)
            }

            Button(
                onClick = { viewModel.initiatePayment() },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Click to Pay",
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
                )
            }
        }

        // Loader Overlay
        if (isLoading) {
            PaymentLoader()
        }
    }
}


@Composable
fun PaymentContent(
    deviceBatterInfo: DeviceBatterInfo,
    deviceInfo: DeviceInfo,
    cpuUsageInfo: DeviceCPUUsageInfo,
    amount: String
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Amount: ₹$amount", fontWeight = FontWeight.Bold)

            DeviceInfoRow("Total usage of cpu", "${cpuUsageInfo.totalUsage} %")
            DeviceInfoRow("Battery level", "${deviceBatterInfo.batterLevel} %")
            DeviceInfoRow("Model", deviceInfo.model)
            DeviceInfoRow("Brand", deviceInfo.brand)
            DeviceInfoRow("OS", "${deviceInfo.osVersion} (API ${deviceInfo.apiLevel})")
            DeviceInfoRow("Manufacturer", deviceInfo.manufacturer)
            DeviceInfoRow("Is Emulator", deviceInfo.isEmulator.toString())
        }
    }
}


@Composable
private fun DeviceInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(text = label, fontWeight = FontWeight.Medium)
        Text(text = value, fontWeight = FontWeight.Normal, color = Color(0xFF666666))
    }
}


