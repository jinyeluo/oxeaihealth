package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter.Companion.between
import com.oxeai.healthconnect.models.ActivityMetadata
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.StepsData
import com.oxeai.healthconnect.models.TrackedMetric

import java.util.UUID

class StepsFetcher(context: Context, userId: UUID) : HealthDataFetcher(context, userId) {
    suspend fun getSteps() {
        try {
            val stepsRequest = ReadRecordsRequest(
                recordType = StepsRecord::class,
                timeRangeFilter = between(startTime, endTime)
            )
            val stepsRecords = healthConnectClient.readRecords(stepsRequest)
            val totalSteps = stepsRecords.records.sumOf { it.count }

            val stepsData = StepsData(
                userId = userId,
                timestamp = endTime,
                source = DataSource.GOOGLE,
                steps = TrackedMetric(
                    count = totalSteps.toInt(),
                    sources = listOf("GoogleFit")
                ),
                metadata = ActivityMetadata(
                    devices = listOf("Unknown"),
                    confidence = DataConfidence.HIGH
                )
            )

            saveDataAsJson(stepsData)
            sendDataToApi(stepsData)

        } catch (e: Exception) {
            Log.e(TAG, "Error reading health data", e)
        }
    }

    companion object {
        private const val TAG = "StepsFetcher"
    }
}
