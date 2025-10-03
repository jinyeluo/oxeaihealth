package com.oxeai.health.worker

import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.SpeedRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.oxeai.health.ActivityMetadata
import com.oxeai.health.DataConfidence
import com.oxeai.health.DataSource
import com.oxeai.health.SpeedData

class SpeedFetcher(context: Context) : HealthDataFetcher(context) {

    suspend fun getSpeed() {
        try {
            val speedRequest = ReadRecordsRequest(
                recordType = SpeedRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
            val speedRecords = healthConnectClient.readRecords(speedRequest)
            val avgSpeed = speedRecords.records.flatMap { it.samples }.map { it.speed.inMetersPerSecond }.average()

            val speedData = SpeedData(
                userId = "user_id", // Replace with actual user ID
                timestamp = endTime,
                source = DataSource.GOOGLE,
                averageSpeed = avgSpeed,
                metadata = ActivityMetadata(
                    devices = listOf("Unknown"),
                    confidence = DataConfidence.HIGH
                )
            )

            saveDataAsJson(speedData)
            sendDataToApi(speedData)
        } catch (e: Exception) {
            Log.e(TAG, "Error reading speed data", e)
        }
    }

    companion object {
        private const val TAG = "SpeedFetcher"
    }
}
