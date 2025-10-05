package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.BasalMetabolicRateRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.oxeai.healthconnect.models.ActivityMetadata
import com.oxeai.healthconnect.models.BasalMetabolicRateData
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.TrackedMeasurement

class BasalMetabolicRateFetcher(context: Context) : HealthDataFetcher(context) {

    suspend fun getBasalMetabolicRate() {
        try {
            val basalMetabolicRateRequest = ReadRecordsRequest(
                recordType = BasalMetabolicRateRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
            val basalMetabolicRateRecords = healthConnectClient.readRecords(basalMetabolicRateRequest)
            val totalBasalMetabolicRate = basalMetabolicRateRecords.records.sumOf { it.basalMetabolicRate.inKilocaloriesPerDay }

            val basalMetabolicRateData = BasalMetabolicRateData(
                userId = "user_id", // Replace with actual user ID
                timestamp = endTime,
                source = DataSource.GOOGLE,
                basalMetabolicRate = TrackedMeasurement(
                    value = totalBasalMetabolicRate,
                    unit = "calories/day",
                    sources = listOf("GoogleFit")
                ),
                metadata = ActivityMetadata(
                    devices = listOf("Unknown"),
                    confidence = DataConfidence.HIGH
                )
            )

            saveDataAsJson(basalMetabolicRateData)
            sendDataToApi(basalMetabolicRateData)
        } catch (e: Exception) {
            Log.e(TAG, "Error reading basal metabolic rate data", e)
        }
    }

    companion object {
        private const val TAG = "BasalMetabolicRateFetcher"
    }
}
