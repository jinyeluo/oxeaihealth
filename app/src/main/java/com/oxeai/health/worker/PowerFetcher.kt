package com.oxeai.health.worker

import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.PowerRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.oxeai.health.ActivityMetadata
import com.oxeai.health.DataConfidence
import com.oxeai.health.DataSource
import com.oxeai.health.PowerData
import com.oxeai.health.TrackedMeasurement

class PowerFetcher(context: Context) : HealthDataFetcher(context) {

    suspend fun getPower() {
        try {
            val powerRequest = ReadRecordsRequest(
                recordType = PowerRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
            val powerRecords = healthConnectClient.readRecords(powerRequest)
            val totalPower = powerRecords.records.sumOf { it.samples.sumOf { it.power.inWatts } }

            val powerData = PowerData(
                userId = "user_id", // Replace with actual user ID
                timestamp = endTime,
                source = DataSource.GOOGLE,
                power = TrackedMeasurement(
                    value = totalPower,
                    unit = "watts",
                    sources = listOf("GoogleFit")
                ),
                metadata = ActivityMetadata(
                    devices = listOf("Unknown"),
                    confidence = DataConfidence.HIGH
                )
            )

            saveDataAsJson(powerData)
            sendDataToApi(powerData)
        } catch (e: Exception) {
            Log.e(TAG, "Error reading power data", e)
        }
    }

    companion object {
        private const val TAG = "PowerFetcher"
    }
}
