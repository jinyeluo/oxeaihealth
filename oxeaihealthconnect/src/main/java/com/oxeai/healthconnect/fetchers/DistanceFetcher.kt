package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.oxeai.healthconnect.models.ActivityMetadata
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.DistanceData
import com.oxeai.healthconnect.models.TrackedMeasurement
import java.util.UUID

class DistanceFetcher(context: Context, userId: UUID) : HealthDataFetcher(context, userId) {

    suspend fun getDistance() {
        try {
            val permissions = healthConnectClient.permissionController.getGrantedPermissions()
            if (HealthPermission.getReadPermission(DistanceRecord::class) !in permissions) {
                Log.w(TAG, "Read permission for DistanceRecord is not granted.")
                return
            }

            val distanceRequest = ReadRecordsRequest(
                recordType = DistanceRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
            val distanceRecords = healthConnectClient.readRecords(distanceRequest)
            val totalDistance = distanceRecords.records.sumOf { it.distance.inMeters }

            val distanceData = DistanceData(
                userId = userId,
                timestamp = endTime,
                source = DataSource.GOOGLE,
                distance = TrackedMeasurement(
                    value = totalDistance,
                    unit = "meters",
                    sources = listOf("GoogleFit")
                ),
                metadata = ActivityMetadata(
                    devices = listOf("Unknown"),
                    confidence = DataConfidence.HIGH
                )
            )

            saveDataAsJson(distanceData)
            sendDataToApi(distanceData)
        } catch (e: Exception) {
            onError(e)
        }
    }

    companion object {
        private const val TAG = "DistanceFetcher"
    }
}
