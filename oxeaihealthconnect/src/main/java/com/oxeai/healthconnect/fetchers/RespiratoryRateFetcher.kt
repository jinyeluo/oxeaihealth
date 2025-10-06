package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.RespiratoryRateRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter.Companion.between
import com.oxeai.healthconnect.models.ActivityMetadata
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.RespiratoryRate
import com.oxeai.healthconnect.models.RespiratoryRateData
import java.util.UUID

class RespiratoryRateFetcher(context: Context, userId: UUID) : HealthDataFetcher(context, userId) {
    suspend fun getRespiratoryRate() {
        try {
            val permissions = healthConnectClient.permissionController.getGrantedPermissions()
            if (HealthPermission.getReadPermission(RespiratoryRateRecord::class) !in permissions) {
                Log.w(TAG, "Read permission for RespiratoryRateRecord is not granted.")
                return
            }

            val respiratoryRateRequest = ReadRecordsRequest(
                recordType = RespiratoryRateRecord::class,
                timeRangeFilter = between(startTime, endTime)
            )
            val respiratoryRateRecords = healthConnectClient.readRecords(respiratoryRateRequest)

            respiratoryRateRecords.records.firstOrNull()?.let { record ->
                val respiratoryRateData = RespiratoryRateData(
                    userId = userId,
                    timestamp = record.time,
                    source = DataSource.GOOGLE,
                    metadata = ActivityMetadata(
                        devices = getDeviceModels(respiratoryRateRecords),
                        confidence = DataConfidence.HIGH
                    ),
                    respiratoryRate = RespiratoryRate(
                        value = record.rate.toInt(),
                        recordedAt = record.time
                    )
                )
                saveDataAsJson(respiratoryRateData)
                sendDataToApi(respiratoryRateData)
            }

        } catch (e: Exception) {
            onError(e)
        }
    }

    companion object {
        private const val TAG = "RespiratoryRateFetcher"
    }
}
