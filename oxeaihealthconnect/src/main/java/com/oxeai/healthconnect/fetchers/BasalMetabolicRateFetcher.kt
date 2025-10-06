package com.oxeai.healthconnect.fetchers

import android.content.Context
import androidx.health.connect.client.records.BasalMetabolicRateRecord
import androidx.health.connect.client.response.ReadRecordsResponse
import com.oxeai.healthconnect.models.BasalMetabolicRateData
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.Metadata
import com.oxeai.healthconnect.models.TrackedMeasurement
import java.util.UUID

class BasalMetabolicRateFetcher(context: Context, userId: UUID) :
    HealthDataFetcher<BasalMetabolicRateRecord>(context, userId, BasalMetabolicRateRecord::class) {

    override fun processRecords(response: ReadRecordsResponse<BasalMetabolicRateRecord>): BasalMetabolicRateData {
        val basalMetabolicRateData = BasalMetabolicRateData(
            userId = userId,
            timestamp = endTime,
            source = DataSource.GOOGLE,
            metadata = Metadata(
                devices = getDeviceModels(response),
                confidence = DataConfidence.HIGH
            )
        )

        response.records.filter { it.basalMetabolicRate.inWatts > 0 }.forEach { record ->
            basalMetabolicRateData.measurements.add(
                TrackedMeasurement(
                    value = record.basalMetabolicRate.inWatts,
                    unit = "w",
                    recordedAt = record.time
                )
            )
        }
        return basalMetabolicRateData
    }
}