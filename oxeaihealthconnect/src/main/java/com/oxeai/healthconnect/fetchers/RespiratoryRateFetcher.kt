package com.oxeai.healthconnect.fetchers

import android.content.Context
import androidx.health.connect.client.records.RespiratoryRateRecord
import androidx.health.connect.client.response.ReadRecordsResponse
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.Metadata
import com.oxeai.healthconnect.models.RespiratoryRateData
import com.oxeai.healthconnect.models.TrackedMeasurement
import java.util.UUID

class RespiratoryRateFetcher(context: Context, userId: UUID) : HealthDataFetcher<RespiratoryRateRecord>(context, userId, RespiratoryRateRecord::class) {

    override fun processRecords(response: ReadRecordsResponse<RespiratoryRateRecord>): RespiratoryRateData {
        val respiratoryRateData = RespiratoryRateData(
            userId = userId,
            timestamp = endTime,
            source = DataSource.GOOGLE,
            metadata = Metadata(
                devices = getDeviceModels(response),
                confidence = DataConfidence.HIGH
            )
        )
        response.records.filter { record -> record.rate > 0 }.forEach { record ->
            respiratoryRateData.measurements.add(
                TrackedMeasurement(
                    value = record.rate,
                    unit = "breaths per minute",
                    recordedAt = record.time
                )
            )
        }
        return respiratoryRateData
    }
}
