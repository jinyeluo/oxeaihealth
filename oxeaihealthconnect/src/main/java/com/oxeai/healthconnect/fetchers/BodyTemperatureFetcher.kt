package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.BodyTemperatureRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter.Companion.between
import com.oxeai.healthconnect.models.ActivityMetadata
import com.oxeai.healthconnect.models.BodyTemperatureData
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.TemperatureReading
import java.util.UUID

class BodyTemperatureFetcher(context: Context, userId: UUID) : HealthDataFetcher(context, userId) {
    suspend fun getBodyTemperature() {
        try {
            val bodyTemperatureRequest = ReadRecordsRequest(
                recordType = BodyTemperatureRecord::class,
                timeRangeFilter = between(startTime, endTime)
            )
            val bodyTemperatureRecords = healthConnectClient.readRecords(bodyTemperatureRequest)

            bodyTemperatureRecords.records.firstOrNull()?.let { record ->
                val bodyTemperatureData = BodyTemperatureData(
                    userId = userId,
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
