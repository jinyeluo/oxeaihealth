package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.oxeai.healthconnect.models.ActiveCaloriesData
import com.oxeai.healthconnect.models.ActivityMetadata
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.TrackedMeasurement

class ActiveCaloriesFetcher(context: Context) : HealthDataFetcher(context) {

    suspend fun getActiveCalories() {
        try {
            val activeCaloriesRequest = ReadRecordsRequest(
                recordType = ActiveCaloriesBurnedRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
            val activeCaloriesRecords = healthConnectClient.readRecords(activeCaloriesRequest)
            val totalActiveCalories = activeCaloriesRecords.records.sumOf { it.energy.inCalories }

            val activeCaloriesData = ActiveCaloriesData(
                userId = "user_id", // Replace with actual user ID
                timestamp = endTime,
                source = DataSource.GOOGLE,
                caloriesActive = TrackedMeasurement(
                    value = totalActiveCalories,
                    unit = "calories",
                    sources = listOf("GoogleFit")
                ),
                metadata = ActivityMetadata(
                    devices = listOf("Unknown"),
                    confidence = DataConfidence.HIGH
                )
            )

            saveDataAsJson(activeCaloriesData)
            sendDataToApi(activeCaloriesData)
        } catch (e: Exception) {
            Log.e(TAG, "Error reading active calories data", e)
        }
    }

    companion object {
        private const val TAG = "ActiveCaloriesFetcher"
    }
}
