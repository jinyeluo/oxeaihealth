package com.oxeai.health.worker

import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.oxeai.health.ActivityMetadata
import com.oxeai.health.DataConfidence
import com.oxeai.health.DataSource
import com.oxeai.health.DistanceData
import com.oxeai.health.TrackedMeasurement

class DistanceFetcher(context: Context) : HealthDataFetcher(context) {

    suspend fun getDistance() {
        try {
            val distanceRequest = ReadRecordsRequest(
                recordType = DistanceRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
            val distanceRecords = healthConnectClient.readRecords(distanceRequest)
            val totalDistance = distanceRecords.records.sumOf { it.distance.inMeters }

            val distanceData = DistanceData(
                userId = "user_id", // Replace with actual user ID
                timestamp = endTime,
                source = DataSource.GOOGLE,
                distance = TrackedMeasurement(
                    value = totalDistance,
                    unit = "meters",
                    sources = listOf("GoogleFit")
                ),
                metadata = ActivityMetadata(
                    devices = listOf("Unknown"),
                    confidence = DataConfidence.HIGH
                )
            )

            saveDataAsJson(distanceData)
            sendDataToApi(distanceData)
        } catch (e: Exception) {
            Log.e(TAG, "Error reading distance data", e)
        }
    }

    companion object {
        private const val TAG = "DistanceFetcher"
    }
}
