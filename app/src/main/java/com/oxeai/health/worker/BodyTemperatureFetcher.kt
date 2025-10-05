package com.oxeai.health.worker

import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.BodyTemperatureRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter.Companion.between
import com.oxeai.health.ActivityMetadata
import com.oxeai.health.BodyTemperatureData
import com.oxeai.health.DataConfidence
import com.oxeai.health.DataSource
import com.oxeai.health.TemperatureReading

class BodyTemperatureFetcher(context: Context) : HealthDataFetcher(context) {
    suspend fun getBodyTemperature() {
        try {
            val bodyTemperatureRequest = ReadRecordsRequest(
                recordType = BodyTemperatureRecord::class,
                timeRangeFilter = between(startTime, endTime)
            )
            val bodyTemperatureRecords = healthConnectClient.readRecords(bodyTemperatureRequest)

            bodyTemperatureRecords.records.firstOrNull()?.let { record ->
                val bodyTemperatureData = BodyTemperatureData(
                    userId = "user_id", // Replace with actual user ID
                    timestamp = record.time,
                    source = DataSource.GOOGLE,
                    metadata = ActivityMetadata(
                        devices = listOf(record.metadata.device.toString()),
                        confidence = DataConfidence.HIGH
                    ),
                    bodyTemperature = TemperatureReading(
                        value = record.temperature.inCelsius,
                        unit = "celsius",
                        recordedAt = record.time
                    )
                )
                saveDataAsJson(bodyTemperatureData)
                sendDataToApi(bodyTemperatureData)
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error reading health data", e)
        }
    }

    companion object {
        private const val TAG = "BodyTemperatureFetcher"
    }
}
