package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter.Companion.between
import com.oxeai.healthconnect.models.ActivityMetadata
import com.oxeai.healthconnect.models.BloodGlucoseData
import com.oxeai.healthconnect.models.BloodGlucoseReading
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.GlucoseContext

class BloodGlucoseFetcher(context: Context) : HealthDataFetcher(context) {
    suspend fun getBloodGlucose() {
        try {
            val bloodGlucoseRequest = ReadRecordsRequest(
                recordType = BloodGlucoseRecord::class,
                timeRangeFilter = between(startTime, endTime)
            )
            val bloodGlucoseRecords = healthConnectClient.readRecords(bloodGlucoseRequest)

            // In this example, we're just taking the first record if it exists.
            // You might want to process all records or the most recent one.
            bloodGlucoseRecords.records.firstOrNull()?.let { record ->
                val bloodGlucoseData = BloodGlucoseData(
                    userId = "user_id", // Replace with actual user ID
                    timestamp = record.time,
                    source = DataSource.GOOGLE,
                    metadata = ActivityMetadata(
                        devices = listOf(record.metadata.device.toString()),
                        confidence = DataConfidence.HIGH
                    ),
                    bloodGlucose = BloodGlucoseReading(
                        value = record.level.inMilligramsPerDeciliter,
                        unit = "mg/dL",
                        context = GlucoseContext.RANDOM, // This is an assumption, you might need to map it
                        recordedAt = record.time
                    )
                )
                saveDataAsJson(bloodGlucoseData)
                sendDataToApi(bloodGlucoseData)
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error reading health data", e)
        }
    }

    companion object {
        private const val TAG = "BloodGlucoseFetcher"
    }
}
