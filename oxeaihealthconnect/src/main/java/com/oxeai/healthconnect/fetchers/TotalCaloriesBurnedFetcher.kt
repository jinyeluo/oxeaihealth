package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.oxeai.healthconnect.models.ActivityMetadata
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.TotalCaloriesBurnedData
import java.util.UUID

class TotalCaloriesBurnedFetcher(context: Context, userId: UUID) : HealthDataFetcher(context, userId) {

    suspend fun getTotalCaloriesBurned() {
        try {
            val totalCaloriesRequest = ReadRecordsRequest(
                recordType = TotalCaloriesBurnedRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
            val totalCaloriesRecords = healthConnectClient.readRecords(totalCaloriesRequest)
            val totalCalories = totalCaloriesRecords.records.sumOf { it.energy.inCalories }

            val totalCaloriesBurnedData = TotalCaloriesBurnedData(
                userId = userId,
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
