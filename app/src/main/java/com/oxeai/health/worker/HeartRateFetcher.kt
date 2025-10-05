package com.oxeai.health.worker

import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.oxeai.health.models.ActivityMetadata
import com.oxeai.health.models.DataConfidence
import com.oxeai.health.models.DataSource
import com.oxeai.health.models.HeartRateData

class HeartRateFetcher(context: Context) : HealthDataFetcher(context) {

    suspend fun getHeartRate() {
        try {
            val heartRateRequest = ReadRecordsRequest(
                recordType = HeartRateRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
            val heartRateRecords = healthConnectClient.readRecords(heartRateRequest)
            val samples = heartRateRecords.records.flatMap { it.samples }
            val avgHeartRate = samples.map { it.beatsPerMinute }.average()
            val minHeartRate = samples.minOfOrNull { it.beatsPerMinute } ?: 0
            val maxHeartRate = samples.maxOfOrNull { it.beatsPerMinute } ?: 0

            val heartRateData = HeartRateData(
                userId = "user_id", // Replace with actual user ID
                timestamp = endTime,
                source = DataSource.GOOGLE,
                averageBpm = avgHeartRate,
                minBpm = minHeartRate,
                maxBpm = maxHeartRate,
                metadata = ActivityMetadata(
                    devices = listOf("Unknown"),
                    confidence = DataConfidence.HIGH
                )
            )

            saveDataAsJson(heartRateData)
            sendDataToApi(heartRateData)
        } catch (e: Exception) {
            Log.e(TAG, "Error reading heart rate data", e)
        }
    }

    companion object {
        private const val TAG = "HeartRateFetcher"
    }
}
