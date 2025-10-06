package com.oxeai.healthconnect.fetchers

import android.content.Context
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.response.ReadRecordsResponse
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.IntervalMeasurement
import com.oxeai.healthconnect.models.Metadata
import com.oxeai.healthconnect.models.TotalCaloriesData
import java.util.UUID

class TotalCaloriesBurnedFetcher(context: Context, userId: UUID) :
    HealthDataFetcher<TotalCaloriesBurnedRecord>(context, userId, TotalCaloriesBurnedRecord::class) {

    override fun processRecords(response: ReadRecordsResponse<TotalCaloriesBurnedRecord>): TotalCaloriesData {
        val totalCaloriesData = TotalCaloriesData(
            userId = userId,
            timestamp = endTime,
            source = DataSource.GOOGLE,
            metadata = Metadata(
                devices = getDeviceModels(response),
                confidence = DataConfidence.HIGH
            )
        )
        response.records.filter { record -> record.energy.inJoules > 0 }.forEach { record ->
            totalCaloriesData.measurements.add(
                IntervalMeasurement(
                    value = record.energy.inJoules,
                    unit = "J",
                    startTime = record.startTime,
                    endTime = record.endTime,
                ),
            )
        }
        return totalCaloriesData
    }
}
