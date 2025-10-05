package com.oxeai.health.worker

import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.HeightRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter.Companion.between
import com.oxeai.health.ActivityMetadata
import com.oxeai.health.DataConfidence
import com.oxeai.health.DataSource
import com.oxeai.health.HeightData
import com.oxeai.health.TrackedMetric

class HeightFetcher(context: Context) : HealthDataFetcher(context) {
    suspend fun getHeight() {
        try {
            val heightRequest = ReadRecordsRequest(
                recordType = HeightRecord::class,
                timeRangeFilter = between(startTime, endTime)
            )
            val heightRecords = healthConnectClient.readRecords(heightRequest)
            val totalHeight = heightRecords.records.sumOf { it.height.inMeters }

            val heightData = HeightData(
                userId = "user_id", // Replace with actual user ID
                timestamp = endTime,
                source = DataSource.GOOGLE,
                height = TrackedMetric(
                    count = totalHeight.toInt(),
                    sources = listOf("GoogleFit")
                ),
                metadata = ActivityMetadata(
                    devices = listOf("Unknown"),
                    confidence = DataConfidence.HIGH
                )
            )

            saveDataAsJson(heightData)
            sendDataToApi(heightData)

        } catch (e: Exception) {
            Log.e(TAG, "Error reading health data", e)
        }
    }

    companion object {
        private const val TAG = "HeightFetcher"
    }
}
