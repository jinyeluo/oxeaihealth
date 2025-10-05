package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.oxeai.healthconnect.models.ActivityMetadata
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.SimpleMetric
import com.oxeai.healthconnect.models.StandHoursData
import java.time.Duration

class StandHoursFetcher(context: Context) : HealthDataFetcher(context) {

    suspend fun getStandHours() {
        try {
            // Note: Health Connect does not have a direct equivalent for "Stand Hours".
            // We are using ExerciseSessionRecord with a specific type as a proxy.
            // This may not be an accurate representation of stand hours.
            val standHoursRequest = ReadRecordsRequest(
                recordType = ExerciseSessionRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
            val standHoursRecords = healthConnectClient.readRecords(standHoursRequest)
            val totalStandHours = standHoursRecords.records
                .filter { it.exerciseType == ExerciseSessionRecord.EXERCISE_TYPE_CALISTHENICS }
                .sumOf { Duration.between(it.startTime, it.endTime).toHours() }

            val standHoursData = StandHoursData(
                userId = "user_id", // Replace with actual user ID
                timestamp = endTime,
                source = DataSource.GOOGLE,
                standHours = SimpleMetric(
                    count = totalStandHours.toInt(),
                    source = "GoogleFit"
                ),
                metadata = ActivityMetadata(
                    devices = listOf("Unknown"),
                    confidence = DataConfidence.LOW // Confidence is low due to proxy metric
                )
            )

            saveDataAsJson(standHoursData)
            sendDataToApi(standHoursData)
        } catch (e: Exception) {
            Log.e(TAG, "Error reading stand hours data", e)
        }
    }

    companion object {
        private const val TAG = "StandHoursFetcher"
    }
}
