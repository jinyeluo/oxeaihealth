package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.BodyTemperatureRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter.Companion.between
import com.oxeai.healthconnect.models.ActivityMetadata
import com.oxeai.healthconnect.models.BodyTemperatureData
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.TemperatureReading
import java.util.UUID

class BodyTemperatureFetcher(context: Context, userId: UUID) : HealthDataFetcher(context, userId) {
    suspend fun getBodyTemperature() {
        try {
            val permissions = healthConnectClient.permissionController.getGrantedPermissions()
            if (HealthPermission.getReadPermission(BodyTemperatureRecord::class) !in permissions) {
                Log.w(TAG, "Read permission for BodyTemperatureRecord is not granted.")
                return
            }

            val bodyTemperatureRequest = ReadRecordsRequest(
                recordType = BodyTemperatureRecord::class,
                timeRangeFilter = between(startTime, endTime)
            )
            val readRecordsResponse = healthConnectClient.readRecords(bodyTemperatureRequest)
            if (readRecordsResponse.records.isEmpty()) {
                return
            }

            val bodyTemperatureData = BodyTemperatureData(
                userId = userId,
                timestamp = endTime,
                source = DataSource.GOOGLE,
                metadata = ActivityMetadata(
                    devices = getDeviceModels(readRecordsResponse),
                    confidence = DataConfidence.HIGH
                ),
                bodyTemperature = ArrayList()
            )
            readRecordsResponse.records.forEach { record ->
                bodyTemperatureData.bodyTemperature.add(
                    TemperatureReading(
                        value = record.temperature.inCelsius,
                        unit = "celsius",
                        recordedAt = record.time
                    )
                )
            }
            saveDataAsJson(bodyTemperatureData)
            sendDataToApi(bodyTemperatureData)
        } catch (e: Exception) {
            onError(e)
        }
    }

    companion object {
        private const val TAG = "BodyTemperatureFetcher"
    }
}
