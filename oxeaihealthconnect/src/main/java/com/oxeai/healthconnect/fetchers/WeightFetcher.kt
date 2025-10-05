package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter.Companion.between
import com.oxeai.healthconnect.models.ActivityMetadata
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.TrackedMetric
import com.oxeai.healthconnect.models.WeightData
import java.util.UUID

class WeightFetcher(context: Context, userId: UUID) : HealthDataFetcher(context, userId) {
    suspend fun getWeight() {
        try {
            val weightRequest = ReadRecordsRequest(
                recordType = WeightRecord::class,
                timeRangeFilter = between(startTime, endTime)
            )
            val weightRecords = healthConnectClient.readRecords(weightRequest)
            val totalWeight = weightRecords.records.sumOf { it.weight.inKilograms }

            val weightData = WeightData(
                userId = userId,
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
