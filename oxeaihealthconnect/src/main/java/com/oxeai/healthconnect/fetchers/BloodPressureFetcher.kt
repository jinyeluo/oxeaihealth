package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter.Companion.between
import com.oxeai.healthconnect.models.ActivityMetadata
import com.oxeai.healthconnect.models.BloodPressureData
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.TrackedMetric
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
            val systolic = bloodPressureRecords.records.map { it.systolic.inMillimetersOfMercury }.average()
            val diastolic = bloodPressureRecords.records.map { it.diastolic.inMillimetersOfMercury }.average()

            val bloodPressureData = BloodPressureData(
                userId = userId,
                timestamp = endTime,
                source = DataSource.GOOGLE,
                systolic = TrackedMetric(
                    count = systolic.toInt(),
                    sources = listOf("GoogleFit")
                ),
                diastolic = TrackedMetric(
                    count = diastolic.toInt(),
                    sources = listOf("GoogleFit")
                ),
                metadata = ActivityMetadata(
                    devices = listOf("Unknown"),
                    confidence = DataConfidence.HIGH
                )
            )

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
