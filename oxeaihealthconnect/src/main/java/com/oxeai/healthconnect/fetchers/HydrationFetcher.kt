package com.oxeai.healthconnect.fetchers

import android.content.Context
import androidx.health.connect.client.records.HydrationRecord
import androidx.health.connect.client.response.ReadRecordsResponse
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.HydrationData
import com.oxeai.healthconnect.models.IntervalMeasurement
import com.oxeai.healthconnect.models.Metadata
import java.util.UUID

class HydrationFetcher(context: Context, userId: UUID) : HealthDataFetcher<HydrationRecord>(context, userId, HydrationRecord::class) {

    override fun processRecords(response: ReadRecordsResponse<HydrationRecord>): HydrationData {
        val hydrationData = HydrationData(
            userId = userId,
            timestamp = endTime,
            source = DataSource.GOOGLE,
            metadata = Metadata(
                devices = getDeviceModels(response),
                confidence = DataConfidence.HIGH
            )
        )
        response.records.filter { record -> record.volume.inLiters > 0 }.forEach { record ->
            hydrationData.measurements.add(
                IntervalMeasurement(
                    value = record.volume.inLiters,
                    unit = "l",
                    startTime = record.startTime,
                    endTime = record.endTime
                )
            )
        }
        return hydrationData
    }
}