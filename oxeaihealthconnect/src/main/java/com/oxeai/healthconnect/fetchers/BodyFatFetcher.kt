package com.oxeai.healthconnect.fetchers

import android.content.Context
import androidx.health.connect.client.records.BodyFatRecord
import androidx.health.connect.client.response.ReadRecordsResponse
import com.oxeai.healthconnect.models.BodyFatData
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.Metadata
import com.oxeai.healthconnect.models.PercentageMeasurement
import java.util.UUID

class BodyFatFetcher(context: Context, userId: UUID) : HealthDataFetcher<BodyFatRecord>(context, userId, BodyFatRecord::class) {

    override fun processRecords(response: ReadRecordsResponse<BodyFatRecord>): BodyFatData {
        val bodyFatData = BodyFatData(
            userId = userId,
            timestamp = endTime,
            source = DataSource.GOOGLE,
            measurements = ArrayList(),
            metadata = Metadata(
                devices = getDeviceModels(response),
                confidence = DataConfidence.HIGH
            )
        )
        response.records.filter { record -> record.percentage.value > 0 }.forEach { record ->
            if (!record.percentage.value.isZero()) {
                bodyFatData.measurements.add(PercentageMeasurement(record.percentage.value, record.time))
            }
        }
        return bodyFatData
    }
}

