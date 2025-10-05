package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.health.connect.client.permission.HealthPermission
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
            val permissions = healthConnectClient.permissionController.getGrantedPermissions()
            if (HealthPermission.getReadPermission(StepsRecord::class) !in permissions) {
                Log.w(TAG, "Read permission for StepsRecord is not granted.")
                return
            }

            val stepsRequest = ReadRecordsRequest(
                recordType = StepsRecord::class,
                timeRangeFilter = between(startTime, endTime)
            )
            val stepsRecords = healthConnectClient.readRecords(stepsRequest)
            val totalSteps = stepsRecords.records.sumOf { it.count }
            if (totalSteps == 0L) {
                Log.w(TAG, "No steps data available.")
                return
            }

            val stepsData = StepsData(
                userId = userId,
                timestamp = endTime,
                source = DataSource.GOOGLE,
                steps = TrackedMetric(
                    count = totalSteps.toInt(),
                ),
                startTime = startTime,
                endTime = endTime,
                metadata = ActivityMetadata(
                    devices = getDeviceModels(stepsRecords),
                    confidence = DataConfidence.HIGH
                )
            )

            saveDataAsJson(stepsData)
            sendDataToApi(stepsData)

        } catch (e: Exception) {
            onError(e)
        }
    }

    companion object {
        private const val TAG = "StepsFetcher"
    }
}
