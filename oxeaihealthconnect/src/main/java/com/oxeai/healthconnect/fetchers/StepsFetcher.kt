package com.oxeai.healthconnect.fetchers

import android.content.Context
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.response.ReadRecordsResponse
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.Metadata
import com.oxeai.healthconnect.models.StepsData
import com.oxeai.healthconnect.models.TrackedMetric
import java.util.UUID

class StepsFetcher(context: Context, userId: UUID) : HealthDataFetcher<StepsRecord>(context, userId, StepsRecord::class) {

    override fun processRecords(response: ReadRecordsResponse<StepsRecord>): StepsData {
        val stepsData = StepsData(
            userId = userId,
            timestamp = endTime,
            source = DataSource.GOOGLE,
            metadata = Metadata(
                devices = getDeviceModels(response),
                confidence = DataConfidence.HIGH
            )
        )
        response.records.filter { record -> record.count > 0 }.forEach { record ->
            stepsData.measurements.add(
                TrackedMetric(
                    count = record.count.toInt(),
                    startTime = record.startTime,
                    endTime = record.endTime
                )
            )
        }
        return stepsData
    }
}
