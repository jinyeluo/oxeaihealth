package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.SpeedRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.oxeai.healthconnect.models.ActivityMetadata
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.SpeedData
import java.util.UUID

class SpeedFetcher(context: Context, userId: UUID) : HealthDataFetcher(context, userId) {

    suspend fun getSpeed() {
        try {
            val speedRequest = ReadRecordsRequest(
                recordType = SpeedRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
            val speedRecords = healthConnectClient.readRecords(speedRequest)
            val avgSpeed = speedRecords.records.flatMap { it.samples }.map { it.speed.inMetersPerSecond }.average()

            val speedData = SpeedData(
                userId = userId,
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
