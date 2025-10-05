package com.oxeai.health.worker

import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter.Companion.between
import com.oxeai.health.models.ActivityMetadata
import com.oxeai.health.models.DataConfidence
import com.oxeai.health.models.DataSource
import com.oxeai.health.models.StepsData
import com.oxeai.health.models.TrackedMetric

class StepsFetcher(context: Context) : HealthDataFetcher(context) {
    suspend fun getSteps() {
        try {
            val stepsRequest = ReadRecordsRequest(
                recordType = StepsRecord::class,
                timeRangeFilter = between(startTime, endTime)
            )
            val stepsRecords = healthConnectClient.readRecords(stepsRequest)
            val totalSteps = stepsRecords.records.sumOf { it.count }

            val stepsData = StepsData(
                userId = "user_id", // Replace with actual user ID
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
