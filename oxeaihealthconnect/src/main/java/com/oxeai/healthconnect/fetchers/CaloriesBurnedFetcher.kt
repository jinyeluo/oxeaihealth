package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.oxeai.healthconnect.models.ActivityMetadata
import com.oxeai.healthconnect.models.CaloriesData
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.TrackedMeasurement
import java.util.UUID

class CaloriesBurnedFetcher(context: Context, userId: UUID) : HealthDataFetcher(context, userId) {

    suspend fun getCaloriesBurned() {
        try {
            val permissions = healthConnectClient.permissionController.getGrantedPermissions()
            if (HealthPermission.getReadPermission(TotalCaloriesBurnedRecord::class) !in permissions) {
                Log.w(TAG, "Read permission for TotalCaloriesBurnedRecord is not granted.")
                return
            }

            if (HealthPermission.getReadPermission(ActiveCaloriesBurnedRecord::class) !in permissions) {
                Log.w(TAG, "Read permission for ActiveCaloriesBurnedRecord is not granted.")
                return
            }

            val totalCaloriesRequest = ReadRecordsRequest(
                recordType = TotalCaloriesBurnedRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
            val totalCaloriesRecords = healthConnectClient.readRecords(totalCaloriesRequest)
            val totalCalories = totalCaloriesRecords.records.sumOf { it.energy.inJoules }

            val activeCaloriesRequest = ReadRecordsRequest(
                recordType = ActiveCaloriesBurnedRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
            val activeCaloriesRecords = healthConnectClient.readRecords(activeCaloriesRequest)
            val activeCalories = activeCaloriesRecords.records.sumOf { it.energy.inJoules }

            val caloriesData = CaloriesData(
                userId = userId,
                timestamp = endTime,
                source = DataSource.GOOGLE,
                metadata = ActivityMetadata(
                    devices = listOf("Unknown"),
                    confidence = DataConfidence.HIGH
                ),
                caloriesActive = TrackedMeasurement(
                    value = activeCalories,
                    unit = "J"
                ),
                caloriesTotal = TrackedMeasurement(
                    value = totalCalories,
                    unit = "J"
                ),
                startTime = startTime,
                endTime = endTime
            )

            saveDataAsJson(caloriesData)
            sendDataToApi(caloriesData)
        } catch (e: Exception) {
            onError(e)
        }
    }

    companion object {
        private const val TAG = "CaloriesFetcher"
    }
}
