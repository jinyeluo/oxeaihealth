package com.oxeai.health.worker

import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.MenstruationFlowRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.oxeai.health.models.ActivityMetadata
import com.oxeai.health.models.DataConfidence
import com.oxeai.health.models.DataSource
import com.oxeai.health.models.MenstruationData

class MenstruationFetcher(context: Context) : HealthDataFetcher(context) {

    suspend fun getMenstruation() {
        try {
            val menstruationRequest = ReadRecordsRequest(
                recordType = MenstruationFlowRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
            val menstruationRecords = healthConnectClient.readRecords(menstruationRequest)

            val menstruationData = menstruationRecords.records.map { record ->
                MenstruationData(
                    userId = "user_id", // Replace with actual user ID
                    timestamp = record.time,
                    source = DataSource.GOOGLE,
                    flow = record.flow.toString(),
                    metadata = ActivityMetadata(
                        devices = listOf(record.metadata.device?.manufacturer ?: "Unknown"),
                        confidence = DataConfidence.HIGH
                    )
                )
            }

            menstruationData.forEach {
                saveDataAsJson(it)
                sendDataToApi(it)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error reading menstruation data", e)
        }
    }

    companion object {
        private const val TAG = "MenstruationFetcher"
    }
}
