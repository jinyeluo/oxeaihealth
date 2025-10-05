package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.BoneMassRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter.Companion.between
import com.oxeai.healthconnect.models.ActivityMetadata
import com.oxeai.healthconnect.models.BoneMassData
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.TrackedMetric
import java.util.UUID

class BoneMassFetcher(context: Context, userId: UUID) : HealthDataFetcher(context, userId) {
    suspend fun getBoneMass() {
        try {
            val permissions = healthConnectClient.permissionController.getGrantedPermissions()
            if (HealthPermission.getReadPermission(BoneMassRecord::class) !in permissions) {
                Log.w(TAG, "Read permission for BoneMassRecord is not granted.")
                return
            }

            val boneMassRequest = ReadRecordsRequest(
                recordType = BoneMassRecord::class,
                timeRangeFilter = between(startTime, endTime)
            )
            val boneMassRecords = healthConnectClient.readRecords(boneMassRequest)
            val totalBoneMass = boneMassRecords.records.sumOf { it.mass.inKilograms }

            val boneMassData = BoneMassData(
                userId = userId,
                timestamp = endTime,
                source = DataSource.GOOGLE,
                boneMass = TrackedMetric(
                    count = totalBoneMass.toInt(),
                    sources = listOf("GoogleFit")
                ),
                metadata = ActivityMetadata(
                    devices = listOf("Unknown"),
                    confidence = DataConfidence.HIGH
                )
            )

            saveDataAsJson(boneMassData)
            sendDataToApi(boneMassData)

        } catch (e: Exception) {
            onError(e)
        }
    }

    companion object {
        private const val TAG = "BoneMassFetcher"
    }
}
