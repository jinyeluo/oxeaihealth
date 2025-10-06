package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.LeanBodyMassRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter.Companion.between
import com.oxeai.healthconnect.models.ActivityMetadata
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.LeanBodyMassData
import java.util.UUID

class LeanBodyMassFetcher(context: Context, userId: UUID) : HealthDataFetcher(context, userId) {
    suspend fun getLeanBodyMass() {
        try {
            val permissions = healthConnectClient.permissionController.getGrantedPermissions()
            if (HealthPermission.getReadPermission(LeanBodyMassRecord::class) !in permissions) {
                Log.w(TAG, "Read permission for LeanBodyMassRecord is not granted.")
                return
            }

            val leanBodyMassRequest = ReadRecordsRequest(
                recordType = LeanBodyMassRecord::class,
                timeRangeFilter = between(startTime, endTime)
            )
            val leanBodyMassRecords = healthConnectClient.readRecords(leanBodyMassRequest)
            val averageLeanBodyMass = leanBodyMassRecords.records.map { it.mass.inGrams }.average()

            if (averageLeanBodyMass.isNaN()) {
                return
            }

            val leanBodyMassData = LeanBodyMassData(
                userId = userId,
                timestamp = endTime,
                source = DataSource.GOOGLE,
                leanBodyMass = averageLeanBodyMass,
                metadata = ActivityMetadata(
                    devices = getDeviceModels(leanBodyMassRecords),
                    confidence = DataConfidence.HIGH
                )
            )

            saveDataAsJson(leanBodyMassData)
            sendDataToApi(leanBodyMassData)

        } catch (e: Exception) {
            onError(e)
        }
    }

    companion object {
        private const val TAG = "LeanBodyMassFetcher"
    }
}
