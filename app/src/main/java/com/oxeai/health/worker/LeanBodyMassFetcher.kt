package com.oxeai.health.worker

import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.LeanBodyMassRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter.Companion.between
import com.oxeai.health.DataSource
import com.oxeai.health.LeanBodyMassData

class LeanBodyMassFetcher(private val context: Context) : HealthDataFetcher(context) {
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
                userId = "user_id", // Replace with actual user ID
                timestamp = endTime,
                source = DataSource.GOOGLE,
                leanBodyMass = averageLeanBodyMass,
                metadata = com.oxeai.health.ActivityMetadata(
                    devices = listOf("Unknown"),
                    confidence = com.oxeai.health.DataConfidence.HIGH
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
