package com.oxeai.health.worker

import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter.Companion.between
import com.oxeai.health.models.ActivityMetadata
import com.oxeai.health.models.DataConfidence
import com.oxeai.health.models.DataSource
import com.oxeai.health.models.TrackedMetric
import com.oxeai.health.models.WeightData

class WeightFetcher(context: Context) : HealthDataFetcher(context) {
    suspend fun getWeight() {
        try {
            val weightRequest = ReadRecordsRequest(
                recordType = WeightRecord::class,
                timeRangeFilter = between(startTime, endTime)
            )
            val weightRecords = healthConnectClient.readRecords(weightRequest)
            val totalWeight = weightRecords.records.sumOf { it.weight.inKilograms }

            val weightData = WeightData(
                userId = "user_id", // Replace with actual user ID
                timestamp = endTime,
                source = DataSource.GOOGLE,
                weight = TrackedMetric(
                    count = totalWeight.toInt(),
                    sources = listOf("GoogleFit")
                ),
                metadata = ActivityMetadata(
                    devices = listOf("Unknown"),
                    confidence = DataConfidence.HIGH
                )
            )

            saveDataAsJson(weightData)
            sendDataToApi(weightData)

        } catch (e: Exception) {
            Log.e(TAG, "Error reading health data", e)
        }
    }

    companion object {
        private const val TAG = "WeightFetcher"
    }
}
