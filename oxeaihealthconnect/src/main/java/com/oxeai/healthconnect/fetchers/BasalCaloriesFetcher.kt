package com.oxeai.healthconnect.fetchers
import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.BasalMetabolicRateRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.oxeai.healthconnect.models.ActivityMetadata
import com.oxeai.healthconnect.models.BasalCaloriesData
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.TrackedMeasurement
import java.util.UUID

class BasalCaloriesFetcher(context: Context, userId: UUID) : HealthDataFetcher(context, userId) {

    suspend fun getBasalCalories() {
        try {
            val basalMetabolicRateRequest = ReadRecordsRequest(
                recordType = BasalMetabolicRateRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
            val basalMetabolicRateRecords = healthConnectClient.readRecords(basalMetabolicRateRequest)
            val avgBasalMetabolicRate = basalMetabolicRateRecords.records.map { it.basalMetabolicRate.inKilocaloriesPerDay }.average()

            val basalCaloriesData = BasalCaloriesData(
                userId = userId,
                timestamp = endTime,
                source = DataSource.GOOGLE,
                caloriesBasal = TrackedMeasurement(
                    value = avgBasalMetabolicRate,
                    unit = "kcal/day",
                    sources = listOf("GoogleFit")
                ),
                metadata = ActivityMetadata(
                    devices = listOf("Unknown"),
                    confidence = DataConfidence.HIGH
                )
            )

            saveDataAsJson(basalCaloriesData)
            sendDataToApi(basalCaloriesData)
        } catch (e: Exception) {
            Log.e(TAG, "Error reading basal calories data", e)
        }
    }

    companion object {
        private const val TAG = "BasalCaloriesFetcher"
    }
}
