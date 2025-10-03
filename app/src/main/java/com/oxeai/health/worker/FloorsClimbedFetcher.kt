package com.oxeai.health.worker

import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.FloorsClimbedRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.oxeai.health.ActivityMetadata
import com.oxeai.health.DataConfidence
import com.oxeai.health.DataSource
import com.oxeai.health.FloorsClimbedData
import com.oxeai.health.TrackedMetric

class FloorsClimbedFetcher(context: Context) : HealthDataFetcher(context) {

    suspend fun getFloorsClimbed() {
        try {
            val floorsClimbedRequest = ReadRecordsRequest(
                recordType = FloorsClimbedRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
            val floorsClimbedRecords = healthConnectClient.readRecords(floorsClimbedRequest)
            val totalFloorsClimbed = floorsClimbedRecords.records.sumOf { it.floors.toInt() }

            val floorsClimbedData = FloorsClimbedData(
                userId = "user_id", // Replace with actual user ID
                timestamp = endTime,
                source = DataSource.GOOGLE,
                floorsClimbed = TrackedMetric(
                    count = totalFloorsClimbed,
                    sources = listOf("GoogleFit")
                ),
                metadata = ActivityMetadata(
                    devices = listOf("Unknown"),
                    confidence = DataConfidence.HIGH
                )
            )

            saveDataAsJson(floorsClimbedData)
            sendDataToApi(floorsClimbedData)
        } catch (e: Exception) {
            Log.e(TAG, "Error reading floors climbed data", e)
        }
    }

    companion object {
        private const val TAG = "FloorsClimbedFetcher"
    }
}
