package com.oxeai.health.worker

import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.HydrationRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter.Companion.between
import com.oxeai.health.models.ActivityMetadata
import com.oxeai.health.models.DataConfidence
import com.oxeai.health.models.DataSource
import com.oxeai.health.models.Hydration
import com.oxeai.health.models.HydrationData

class HydrationFetcher(context: Context) : HealthDataFetcher(context) {
    suspend fun getHydration() {
        try {
            val hydrationRequest = ReadRecordsRequest(
                recordType = HydrationRecord::class,
                timeRangeFilter = between(startTime, endTime)
            )
            val hydrationRecords = healthConnectClient.readRecords(hydrationRequest)

            hydrationRecords.records.firstOrNull()?.let { record ->
                val hydrationData = HydrationData(
                    userId = "user_id", // Replace with actual user ID
                    timestamp = record.endTime,
                    source = DataSource.GOOGLE,
                    metadata = ActivityMetadata(
                        devices = listOf(record.metadata.device.toString()),
                        confidence = DataConfidence.HIGH
                    ),
                    hydration = Hydration(
                        water = record.volume.inLiters,
                        unit = "liters"
                    )
                )
                saveDataAsJson(hydrationData)
                sendDataToApi(hydrationData)
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error reading health data", e)
        }
    }

    companion object {
        private const val TAG = "HydrationFetcher"
    }
}
