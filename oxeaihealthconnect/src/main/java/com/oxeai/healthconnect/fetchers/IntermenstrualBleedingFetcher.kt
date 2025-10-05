package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.IntermenstrualBleedingRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.oxeai.healthconnect.models.ActivityMetadata
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.IntermenstrualBleedingData

class IntermenstrualBleedingFetcher(context: Context) : HealthDataFetcher(context) {

    suspend fun getIntermenstrualBleeding() {
        try {
            val intermenstrualBleedingRequest = ReadRecordsRequest(
                recordType = IntermenstrualBleedingRecord::class,
                timeRangeFilter = TimeRangeFilter.Companion.between(startTime, endTime)
            )
            val intermenstrualBleedingRecords = healthConnectClient.readRecords(intermenstrualBleedingRequest)

            val intermenstrualBleedingData = intermenstrualBleedingRecords.records.map { record ->
                IntermenstrualBleedingData(
                    userId = "user_id", // Replace with actual user ID
                    timestamp = record.time,
                    source = DataSource.GOOGLE,
                    metadata = ActivityMetadata(
                        devices = listOf(record.metadata.device?.manufacturer ?: "Unknown"),
                        confidence = DataConfidence.HIGH
                    )
                )
            }

            intermenstrualBleedingData.forEach {
                saveDataAsJson(it)
                sendDataToApi(it)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error reading intermenstrual bleeding data", e)
        }
    }

    companion object {
        private const val TAG = "IntermenstrualBleedingFetcher"
    }
}