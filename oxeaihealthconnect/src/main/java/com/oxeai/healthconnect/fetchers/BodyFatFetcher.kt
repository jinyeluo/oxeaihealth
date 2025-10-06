package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.BodyFatRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter.Companion.between
import com.oxeai.healthconnect.models.ActivityMetadata
import com.oxeai.healthconnect.models.BodyFat
import com.oxeai.healthconnect.models.BodyFatData
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import java.util.UUID

class BodyFatFetcher(context: Context, userId: UUID) : HealthDataFetcher(context, userId) {
    suspend fun getBodyFat() {
        try {
            val permissions = healthConnectClient.permissionController.getGrantedPermissions()
            if (HealthPermission.getReadPermission(BodyFatRecord::class) !in permissions) {
                Log.w(TAG, "Read permission for BodyFatRecord is not granted.")
                return
            }

            val bodyFatRequest = ReadRecordsRequest(
                recordType = BodyFatRecord::class,
                timeRangeFilter = between(startTime, endTime)
            )
            val readRecordsResponse = healthConnectClient.readRecords(bodyFatRequest)

            if (readRecordsResponse.records.isEmpty()) {
                return
            }

            val bodyFatData = BodyFatData(
                userId = userId,
                timestamp = endTime,
                source = DataSource.GOOGLE,
                bodyFatPercentages = ArrayList(),
                metadata = ActivityMetadata(
                    devices = getDeviceModels(readRecordsResponse),
                    confidence = DataConfidence.HIGH
                )
            )

            readRecordsResponse.records.forEach { record ->
                if (!record.percentage.value.isZero()) {
                    bodyFatData.bodyFatPercentages.add(BodyFat(record.percentage.value, record.time))
                }
            }

            saveDataAsJson(bodyFatData)
            sendDataToApi(bodyFatData)

        } catch (e: Exception) {
            onError(e)
        }
    }

    companion object {
        private const val TAG = "BodyFatFetcher"
    }
}
