package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.BodyFatRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter.Companion.between
import com.oxeai.healthconnect.models.ActivityMetadata
import com.oxeai.healthconnect.models.BodyFatData
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import java.util.UUID

class BodyFatFetcher(context: Context, userId: UUID) : HealthDataFetcher(context, userId) {
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
                userId = userId,
                timestamp = endTime,
                source = DataSource.GOOGLE,
                bodyFatPercentage = averageBodyFat,
                metadata = ActivityMetadata(
                    devices = listOf("Unknown"),
                    confidence = DataConfidence.HIGH
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
