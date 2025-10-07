package com.oxeai.healthconnect.fetchers

import android.content.Context
import androidx.health.connect.client.records.IntermenstrualBleedingRecord
import androidx.health.connect.client.response.ReadRecordsResponse
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.IntermenstrualBleedingData
import com.oxeai.healthconnect.models.Metadata
import com.oxeai.healthconnect.models.TimeMeasurement
import java.util.UUID

class IntermenstrualBleedingFetcher(context: Context, userId: UUID) :
    HealthDataFetcher<IntermenstrualBleedingRecord>(context, userId, IntermenstrualBleedingRecord::class) {

    override fun processRecords(response: ReadRecordsResponse<IntermenstrualBleedingRecord>): List<IntermenstrualBleedingData> {
        val intermenstrualBleedingData = IntermenstrualBleedingData(
            userId = userId,
            timestamp = endTime,
            source = DataSource.GOOGLE,
            metadata = Metadata(
                devices = getDeviceModels(response),
                confidence = DataConfidence.HIGH
            )
        )
        response.records.forEach { record ->
            intermenstrualBleedingData.measurements.add(
                TimeMeasurement(
                    time = record.time
                )
            )
        }
        return listOf(intermenstrualBleedingData)
    }
}