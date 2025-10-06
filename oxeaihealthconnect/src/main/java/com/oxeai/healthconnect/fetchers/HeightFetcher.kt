package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.HeightRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter.Companion.between
import com.oxeai.healthconnect.models.ActivityMetadata
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.HeightData
import com.oxeai.healthconnect.models.TrackedMetric
import java.util.UUID

class HeightFetcher(context: Context, userId: UUID) : HealthDataFetcher(context, userId) {
    suspend fun getHeight() {
        try {
            val permissions = healthConnectClient.permissionController.getGrantedPermissions()
            if (HealthPermission.getReadPermission(HeightRecord::class) !in permissions) {
                Log.w(TAG, "Read permission for HeightRecord is not granted.")
                return
            }

            val heightRequest = ReadRecordsRequest(
                recordType = HeightRecord::class,
                timeRangeFilter = between(startTime, endTime)
            )
            val heightRecords = healthConnectClient.readRecords(heightRequest)
            val totalHeight = heightRecords.records.sumOf { it.height.inMeters }

            val heightData = HeightData(
                userId = userId,
                timestamp = endTime,
                source = DataSource.GOOGLE,
                height = TrackedMetric(
                    count = totalHeight.toInt(),
                ),
                metadata = ActivityMetadata(
                    devices = listOf("Unknown"),
                    confidence = DataConfidence.HIGH
                )
            )

            saveDataAsJson(heightData)
            sendDataToApi(heightData)

        } catch (e: Exception) {
            onError(e)
        }
    }

    companion object {
        private const val TAG = "HeightFetcher"
    }
}
