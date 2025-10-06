package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.BasalMetabolicRateRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.oxeai.healthconnect.models.ActivityMetadata
import com.oxeai.healthconnect.models.BasalMetabolicRateData
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.TrackedMeasurement
import java.util.UUID

class BasalMetabolicRateFetcher(context: Context, userId: UUID) : HealthDataFetcher(context, userId) {

    suspend fun getBasalMetabolicRate() {
        try {
            val permissions = healthConnectClient.permissionController.getGrantedPermissions()
            if (HealthPermission.getReadPermission(BasalMetabolicRateRecord::class) !in permissions) {
                Log.w(TAG, "Read permission for BasalMetabolicRateRecord is not granted.")
                return
            }

            val basalMetabolicRateRequest = ReadRecordsRequest(
                recordType = BasalMetabolicRateRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
            val basalMetabolicRateRecords = healthConnectClient.readRecords(basalMetabolicRateRequest)

            if (basalMetabolicRateRecords.records.isNotEmpty()) {
                for (record in basalMetabolicRateRecords.records) {
                    val basalMetabolicRateData = BasalMetabolicRateData(
                        userId = userId,
                        timestamp = endTime,
                        source = DataSource.GOOGLE,
                        basalMetabolicRate = TrackedMeasurement(
                            value = record.basalMetabolicRate.inKilocaloriesPerDay,
                            unit = "kcal/day",
                        ),
                        metadata = ActivityMetadata(
                            devices = getDeviceModels(basalMetabolicRateRecords),
                            confidence = DataConfidence.HIGH
                        )
                    )
                    saveDataAsJson(basalMetabolicRateData)
                    sendDataToApi(basalMetabolicRateData)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error reading basal metabolic rate data", e)
        }
    }

    companion object {
        private const val TAG = "BasalMetabolicRateFetcher"
    }
}
