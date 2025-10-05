package com.oxeai.health.worker

import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.OxygenSaturationRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter.Companion.between
import com.oxeai.health.ActivityMetadata
import com.oxeai.health.DataConfidence
import com.oxeai.health.DataSource
import com.oxeai.health.OxygenSaturation
import com.oxeai.health.OxygenSaturationData

class OxygenSaturationFetcher(context: Context) : HealthDataFetcher(context) {
    suspend fun getOxygenSaturation() {
        try {
            val oxygenSaturationRequest = ReadRecordsRequest(
                recordType = OxygenSaturationRecord::class,
                timeRangeFilter = between(startTime, endTime)
            )
            val oxygenSaturationRecords = healthConnectClient.readRecords(oxygenSaturationRequest)

            oxygenSaturationRecords.records.firstOrNull()?.let { record ->
                val oxygenSaturationData = OxygenSaturationData(
                    userId = "user_id", // Replace with actual user ID
                    timestamp = record.time,
                    source = DataSource.GOOGLE,
                    metadata = ActivityMetadata(
                        devices = listOf(record.metadata.device.toString()),
                        confidence = DataConfidence.HIGH
                    ),
                    oxygenSaturation = OxygenSaturation(
                        percentage = record.percentage.value,
                        recordedAt = record.time
                    )
                )
                saveDataAsJson(oxygenSaturationData)
                sendDataToApi(oxygenSaturationData)
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error reading health data", e)
        }
    }

    companion object {
        private const val TAG = "OxygenSaturationFetcher"
    }
}
