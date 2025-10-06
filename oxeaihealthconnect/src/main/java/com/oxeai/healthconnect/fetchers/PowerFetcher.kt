package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.PowerRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.oxeai.healthconnect.models.ActivityMetadata
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.PowerData
import com.oxeai.healthconnect.models.TrackedMeasurement
import java.util.UUID

class PowerFetcher(context: Context, userId: UUID) : HealthDataFetcher(context, userId) {

    suspend fun getPower() {
        try {
            val permissions = healthConnectClient.permissionController.getGrantedPermissions()
            if (HealthPermission.getReadPermission(PowerRecord::class) !in permissions) {
                Log.w(TAG, "Read permission for PowerRecord is not granted.")
                return
            }

            val powerRequest = ReadRecordsRequest(
                recordType = PowerRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
            val powerRecords = healthConnectClient.readRecords(powerRequest)
            val totalPower = powerRecords.records.sumOf { it.samples.sumOf { it.power.inWatts } }

            val powerData = PowerData(
                userId = userId,
                timestamp = endTime,
                source = DataSource.GOOGLE,
                measurements = TrackedMeasurement(
                    value = totalPower,
                    unit = "watts",
                ),
                metadata = ActivityMetadata(
                    devices = getDeviceModels(powerRecords),
                    confidence = DataConfidence.HIGH
                )
            )

            saveDataAsJson(powerData)
            sendDataToApi(powerData)
        } catch (e: Exception) {
            onError(e)
        }
    }

    companion object {
        private const val TAG = "PowerFetcher"
    }
}
