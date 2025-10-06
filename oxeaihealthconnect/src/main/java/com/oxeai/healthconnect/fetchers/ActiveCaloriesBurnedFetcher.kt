package com.oxeai.healthconnect.fetchers

import android.content.Context
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.response.ReadRecordsResponse
import com.oxeai.healthconnect.models.ActiveCaloriesData
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.IntervalMeasurement
import com.oxeai.healthconnect.models.Metadata
import java.util.UUID

class ActiveCaloriesBurnedFetcher(context: Context, userId: UUID) :
    HealthDataFetcher<ActiveCaloriesBurnedRecord>(context, userId, ActiveCaloriesBurnedRecord::class) {

    override fun processRecords(response: ReadRecordsResponse<ActiveCaloriesBurnedRecord>): ActiveCaloriesData {
        val activeCaloriesData = ActiveCaloriesData(
            userId = userId,
            timestamp = endTime,
            source = DataSource.GOOGLE,
            metadata = Metadata(
                devices = getDeviceModels(response),
                confidence = DataConfidence.HIGH
            )
        )
        response.records.filter { record -> record.energy.inJoules > 0 }.forEach { record ->
            activeCaloriesData.measurements.add(
                IntervalMeasurement(
                    value = record.energy.inJoules,
                    unit = "J",
                    startTime = record.startTime,
                    endTime = record.endTime,
                ),
            )
        }
        return activeCaloriesData
    }
}
