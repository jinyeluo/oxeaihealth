package com.oxeai.health.worker

import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.oxeai.health.models.ActivityMetadata
import com.oxeai.health.models.DataConfidence
import com.oxeai.health.models.DataSource
import com.oxeai.health.models.TotalCaloriesBurnedData

class TotalCaloriesBurnedFetcher(context: Context) : HealthDataFetcher(context) {

    suspend fun getTotalCaloriesBurned() {
        try {
            val totalCaloriesRequest = ReadRecordsRequest(
                recordType = TotalCaloriesBurnedRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
            val totalCaloriesRecords = healthConnectClient.readRecords(totalCaloriesRequest)
            val totalCalories = totalCaloriesRecords.records.sumOf { it.energy.inCalories }

            val totalCaloriesBurnedData = TotalCaloriesBurnedData(
                userId = "user_id", // Replace with actual user ID
                timestamp = endTime,
                source = DataSource.GOOGLE,
                totalCalories = totalCalories,
                metadata = ActivityMetadata(
                    devices = listOf("Unknown"),
                    confidence = DataConfidence.HIGH
                )
            )

            saveDataAsJson(totalCaloriesBurnedData)
            sendDataToApi(totalCaloriesBurnedData)
        } catch (e: Exception) {
            Log.e(TAG, "Error reading total calories burned data", e)
        }
    }

    companion object {
        private const val TAG = "TotalCaloriesFetcher"
    }
}
