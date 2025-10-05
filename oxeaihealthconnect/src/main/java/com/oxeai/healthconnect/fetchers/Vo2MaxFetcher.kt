package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.Vo2MaxRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.oxeai.healthconnect.models.ActivityMetadata
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.Vo2MaxData
import java.util.UUID

class Vo2MaxFetcher(context: Context, userId: UUID) : HealthDataFetcher(context, userId) {

    suspend fun getVo2Max() {
        try {
            val permissions = healthConnectClient.permissionController.getGrantedPermissions()
            if (HealthPermission.getReadPermission(Vo2MaxRecord::class) !in permissions) {
                Log.w(TAG, "Read permission for Vo2MaxRecord is not granted.")
                return
            }

            val vo2MaxRequest = ReadRecordsRequest(
                recordType = Vo2MaxRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
            val vo2MaxRecords = healthConnectClient.readRecords(vo2MaxRequest)
            val avgVo2Max = vo2MaxRecords.records.map { it.vo2MillilitersPerMinuteKilogram }.average()

            val vo2MaxData = Vo2MaxData(
                userId = userId,
                timestamp = endTime,
                source = DataSource.GOOGLE,
                vo2Max = avgVo2Max,
                metadata = ActivityMetadata(
                    devices = listOf("Unknown"),
                    confidence = DataConfidence.HIGH
                )
            )

            saveDataAsJson(vo2MaxData)
            sendDataToApi(vo2MaxData)
        } catch (e: Exception) {
            onError(e)
        }
    }

    companion object {
        private const val TAG = "Vo2MaxFetcher"
    }
}
