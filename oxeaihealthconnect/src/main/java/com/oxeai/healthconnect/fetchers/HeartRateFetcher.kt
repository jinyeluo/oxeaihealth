package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.oxeai.healthconnect.models.ActivityMetadata
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.HeartRateData
import java.util.UUID

class HeartRateFetcher(context: Context, userId: UUID) : HealthDataFetcher(context, userId) {

    suspend fun getHeartRate() {
        try {
            val permissions = healthConnectClient.permissionController.getGrantedPermissions()
            if (HealthPermission.getReadPermission(HeartRateRecord::class) !in permissions) {
                Log.w(TAG, "Read permission for HeartRateRecord is not granted.")
                return
            }

            val heartRateRequest = ReadRecordsRequest(
                recordType = HeartRateRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
            val heartRateRecords = healthConnectClient.readRecords(heartRateRequest)
            val samples = heartRateRecords.records.flatMap { it.samples }
            val avgHeartRate = samples.map { it.beatsPerMinute }.average()
            val minHeartRate = samples.minOfOrNull { it.beatsPerMinute } ?: 0
            val maxHeartRate = samples.maxOfOrNull { it.beatsPerMinute } ?: 0

            val heartRateData = HeartRateData(
                userId = userId,
                timestamp = endTime,
                source = DataSource.GOOGLE,
                averageBpm = avgHeartRate,
                minBpm = minHeartRate,
                maxBpm = maxHeartRate,
                metadata = ActivityMetadata(
                    devices = getDeviceModels(heartRateRecords),
                    confidence = DataConfidence.HIGH
                )
            )

            saveDataAsJson(heartRateData)
            sendDataToApi(heartRateData)
        } catch (e: Exception) {
            onError(e)
        }
    }

    companion object {
        private const val TAG = "HeartRateFetcher"
    }
}
