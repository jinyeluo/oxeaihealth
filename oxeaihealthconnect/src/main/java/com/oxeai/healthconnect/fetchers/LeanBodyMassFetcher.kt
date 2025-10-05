package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
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
                    devices = listOf("Unknown"),
                    confidence = DataConfidence.HIGH
                )
            )

            saveDataAsJson(leanBodyMassData)
            sendDataToApi(leanBodyMassData)

        } catch (e: Exception) {
            Log.e(TAG, "Error reading health data", e)
        }
    }

    companion object {
        private const val TAG = "LeanBodyMassFetcher"
    }
}
