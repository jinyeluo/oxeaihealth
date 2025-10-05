package com.oxeai.health.worker

import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.RespiratoryRateRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter.Companion.between
import com.oxeai.health.models.ActivityMetadata
import com.oxeai.health.models.DataConfidence
import com.oxeai.health.models.DataSource
import com.oxeai.health.models.RespiratoryRate
import com.oxeai.health.models.RespiratoryRateData

class RespiratoryRateFetcher(context: Context) : HealthDataFetcher(context) {
    suspend fun getRespiratoryRate() {
        try {
            val respiratoryRateRequest = ReadRecordsRequest(
                recordType = RespiratoryRateRecord::class,
                timeRangeFilter = between(startTime, endTime)
            )
            val respiratoryRateRecords = healthConnectClient.readRecords(respiratoryRateRequest)

            respiratoryRateRecords.records.firstOrNull()?.let { record ->
                val respiratoryRateData = RespiratoryRateData(
                    userId = "user_id", // Replace with actual user ID
                    timestamp = record.time,
                    source = DataSource.GOOGLE,
                    metadata = ActivityMetadata(
                        devices = listOf(record.metadata.device.toString()),
                        confidence = DataConfidence.HIGH
                    ),
                    respiratoryRate = RespiratoryRate(
                        value = record.rate.toInt(),
                        recordedAt = record.time
                    )
                )
                saveDataAsJson(respiratoryRateData)
                sendDataToApi(respiratoryRateData)
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error reading health data", e)
        }
    }

    companion object {
        private const val TAG = "RespiratoryRateFetcher"
    }
}
