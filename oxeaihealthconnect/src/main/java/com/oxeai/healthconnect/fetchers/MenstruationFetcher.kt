package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.MenstruationFlowRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.oxeai.healthconnect.models.ActivityMetadata
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.MenstruationData
import java.util.UUID

class MenstruationFetcher(context: Context, userId: UUID) : HealthDataFetcher(context, userId) {

    suspend fun getMenstruation() {
        try {
            val permissions = healthConnectClient.permissionController.getGrantedPermissions()
            if (HealthPermission.getReadPermission(MenstruationFlowRecord::class) !in permissions) {
                Log.w(TAG, "Read permission for MenstruationFlowRecord is not granted.")
                return
            }

            val menstruationRequest = ReadRecordsRequest(
                recordType = MenstruationFlowRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
            val menstruationRecords = healthConnectClient.readRecords(menstruationRequest)

            val menstruationData = menstruationRecords.records.map { record ->
                MenstruationData(
                    userId = userId,
                    timestamp = record.time,
                    source = DataSource.GOOGLE,
                    flow = record.flow.toString(),
                    metadata = ActivityMetadata(
                        devices = listOf(record.metadata.device?.manufacturer ?: "Unknown"),
                        confidence = DataConfidence.HIGH
                    )
                )
            }

            menstruationData.forEach {
                saveDataAsJson(it)
                sendDataToApi(it)
            }
        } catch (e: Exception) {
            onError(e)
        }
    }

    companion object {
        private const val TAG = "MenstruationFetcher"
    }
}
