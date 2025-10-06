package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.HydrationRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter.Companion.between
import com.oxeai.healthconnect.models.ActivityMetadata
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.Hydration
import com.oxeai.healthconnect.models.HydrationData
import java.util.UUID

class HydrationFetcher(context: Context, userId: UUID) : HealthDataFetcher(context, userId) {
    suspend fun getHydration() {
        try {
            val permissions = healthConnectClient.permissionController.getGrantedPermissions()
            if (HealthPermission.getReadPermission(HydrationRecord::class) !in permissions) {
                Log.w(TAG, "Read permission for HydrationRecord is not granted.")
                return
            }

            val hydrationRequest = ReadRecordsRequest(
                recordType = HydrationRecord::class,
                timeRangeFilter = between(startTime, endTime)
            )
            val hydrationRecords = healthConnectClient.readRecords(hydrationRequest)

            hydrationRecords.records.firstOrNull()?.let { record ->
                val hydrationData = HydrationData(
                    userId = userId,
                    timestamp = record.endTime,
                    source = DataSource.GOOGLE,
                    metadata = ActivityMetadata(
                        devices = getDeviceModels(hydrationRecords),
                        confidence = DataConfidence.HIGH
                    ),
                    hydration = Hydration(
                        water = record.volume.inLiters,
                        unit = "liters"
                    )
                )
                saveDataAsJson(hydrationData)
                sendDataToApi(hydrationData)
            }

        } catch (e: Exception) {
            onError(e)
        }
    }

    companion object {
        private const val TAG = "HydrationFetcher"
    }
}
