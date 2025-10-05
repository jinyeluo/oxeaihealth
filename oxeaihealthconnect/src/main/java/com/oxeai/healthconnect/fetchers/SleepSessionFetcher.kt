package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.oxeai.healthconnect.models.ActivityMetadata
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.SleepSessionData
import java.util.UUID

class SleepSessionFetcher(context: Context, userId: UUID) : HealthDataFetcher(context, userId) {

    suspend fun getSleepSession() {
        try {
            val sleepSessionRequest = ReadRecordsRequest(
                recordType = SleepSessionRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
            val sleepSessionRecords = healthConnectClient.readRecords(sleepSessionRequest)

            val sleepSessions = sleepSessionRecords.records.map { record ->
                SleepSessionData(
                    userId = userId,
                    timestamp = record.startTime,
                    source = DataSource.GOOGLE,
                    startTime = record.startTime,
                    endTime = record.endTime,
                    duration = record.endTime.toEpochMilli() - record.startTime.toEpochMilli(),
                    stages = record.stages.joinToString { it.stage.toString() },
                    metadata = ActivityMetadata(
                        devices = listOf(record.metadata.device?.manufacturer ?: "Unknown"),
                        confidence = DataConfidence.HIGH
                    )
                )
            }

            sleepSessions.forEach {
                saveDataAsJson(it)
                sendDataToApi(it)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error reading sleep session data", e)
        }
    }

    companion object {
        private const val TAG = "SleepSessionFetcher"
    }
}
