package com.oxeai.health.worker

import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.BodyFatRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter.Companion.between
import com.oxeai.health.BodyFatData
import com.oxeai.health.DataSource

class BodyFatFetcher(context: Context) : HealthDataFetcher(context) {
    suspend fun getBodyFat() {
        try {
            val bodyFatRequest = ReadRecordsRequest(
                recordType = BodyFatRecord::class,
                timeRangeFilter = between(startTime, endTime)
            )
            val bodyFatRecords = healthConnectClient.readRecords(bodyFatRequest)
            val averageBodyFat = bodyFatRecords.records.map { it.percentage.value }.average()

            if (averageBodyFat.isNaN()) {
                return
            }

            val bodyFatData = BodyFatData(
                userId = "user_id", // Replace with actual user ID
                timestamp = endTime,
                source = DataSource.GOOGLE,
                bodyFatPercentage = averageBodyFat,
                metadata = com.oxeai.health.ActivityMetadata(
                    devices = listOf("Unknown"),
                    confidence = com.oxeai.health.DataConfidence.HIGH
                )
            )

            saveDataAsJson(bodyFatData)
            sendDataToApi(bodyFatData)

        } catch (e: Exception) {
            Log.e(TAG, "Error reading health data", e)
        }
    }

    companion object {
        private const val TAG = "BodyFatFetcher"
    }
}
