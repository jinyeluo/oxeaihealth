package com.oxeai.health.worker

import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.Vo2MaxRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.oxeai.health.models.ActivityMetadata
import com.oxeai.health.models.DataConfidence
import com.oxeai.health.models.DataSource
import com.oxeai.health.models.Vo2MaxData

class Vo2MaxFetcher(context: Context) : HealthDataFetcher(context) {

    suspend fun getVo2Max() {
        try {
            val vo2MaxRequest = ReadRecordsRequest(
                recordType = Vo2MaxRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
            val vo2MaxRecords = healthConnectClient.readRecords(vo2MaxRequest)
            val avgVo2Max = vo2MaxRecords.records.map { it.vo2MillilitersPerMinuteKilogram }.average()

            val vo2MaxData = Vo2MaxData(
                userId = "user_id", // Replace with actual user ID
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
            Log.e(TAG, "Error reading Vo2Max data", e)
        }
    }

    companion object {
        private const val TAG = "Vo2MaxFetcher"
    }
}
