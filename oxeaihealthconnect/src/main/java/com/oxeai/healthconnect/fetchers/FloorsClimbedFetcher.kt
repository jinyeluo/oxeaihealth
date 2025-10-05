package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.FloorsClimbedRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.oxeai.healthconnect.models.ActivityMetadata
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.FloorsClimbedData
import com.oxeai.healthconnect.models.TrackedMetric
import java.util.UUID

class FloorsClimbedFetcher(context: Context, userId: UUID) : HealthDataFetcher(context, userId) {

    suspend fun getFloorsClimbed() {
        try {
            val floorsClimbedRequest = ReadRecordsRequest(
                recordType = FloorsClimbedRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
            val floorsClimbedRecords = healthConnectClient.readRecords(floorsClimbedRequest)
            val totalFloorsClimbed = floorsClimbedRecords.records.sumOf { it.floors.toInt() }

            val floorsClimbedData = FloorsClimbedData(
                userId = userId,
                timestamp = endTime,
                source = DataSource.GOOGLE,
                floorsClimbed = TrackedMetric(
                    count = totalFloorsClimbed,
                    sources = listOf("GoogleFit")
                ),
                metadata = ActivityMetadata(
                    devices = listOf("Unknown"),
                    confidence = DataConfidence.HIGH
                )
            )

            saveDataAsJson(floorsClimbedData)
            sendDataToApi(floorsClimbedData)
        } catch (e: Exception) {
            Log.e(TAG, "Error reading floors climbed data", e)
        }
    }

    companion object {
        private const val TAG = "FloorsClimbedFetcher"
    }
}
