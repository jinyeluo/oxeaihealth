package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter.Companion.between
import com.oxeai.healthconnect.models.ActivityMetadata
import com.oxeai.healthconnect.models.BloodPressure
import com.oxeai.healthconnect.models.BloodPressureData
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import java.util.UUID

class BloodPressureFetcher(context: Context, userId: UUID) : HealthDataFetcher(context, userId) {
    suspend fun getBloodPressure() {
        try {
            val permissions = healthConnectClient.permissionController.getGrantedPermissions()
            if (HealthPermission.getReadPermission(BloodPressureRecord::class) !in permissions) {
                Log.w(TAG, "Read permission for BloodPressureRecord is not granted.")
                return
            }

            val bloodPressureRequest = ReadRecordsRequest(
                recordType = BloodPressureRecord::class,
                timeRangeFilter = between(startTime, endTime)
            )
            val bloodPressureRecords = healthConnectClient.readRecords(bloodPressureRequest)

            if (bloodPressureRecords.records.isEmpty()) {
                return
            }

            val bloodPressureData = BloodPressureData(
                userId = userId,
                timestamp = endTime,
                source = DataSource.GOOGLE,
                bloodPressure = ArrayList(),
                metadata = ActivityMetadata(
                    devices = getDeviceModels(bloodPressureRecords),
                    confidence = DataConfidence.HIGH
                )
            )
            for (record in bloodPressureRecords.records) {
                bloodPressureData.bloodPressure.plus(
                    BloodPressure(
                        record.systolic.inMillimetersOfMercury.toInt(),
                        record.diastolic.inMillimetersOfMercury.toInt(),
                        unit = "mmHg",
                        recordedAt = record.time
                    ),
                )
            }

            saveDataAsJson(bloodPressureData)
            sendDataToApi(bloodPressureData)

        } catch (e: Exception) {
            onError(e)
        }
    }

    companion object {
        private const val TAG = "BloodPressureFetcher"
    }
}
