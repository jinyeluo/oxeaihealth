package com.oxeai.healthconnect.fetchers

import android.content.Context
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.response.ReadRecordsResponse
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.Metadata
import com.oxeai.healthconnect.models.TrackedMeasurement
import com.oxeai.healthconnect.models.WeightData
import java.util.UUID

class WeightFetcher(context: Context, userId: UUID) : HealthDataFetcher<WeightRecord>(context, userId, WeightRecord::class) {

    override fun processRecords(response: ReadRecordsResponse<WeightRecord>): List<WeightData> {
        val weightData = WeightData(
            userId = userId,
            timestamp = endTime,
            source = DataSource.GOOGLE,
            metadata = Metadata(
                devices = getDeviceModels(response),
                confidence = DataConfidence.HIGH
            )
        )
        response.records.filter { record -> record.weight.inKilograms > 0 }.forEach { record ->
            weightData.measurements.add(
                TrackedMeasurement(
                    value = record.weight.inKilograms,
                    unit = "kg",
                    recordedAt = record.time,
                    timeZoneOffset = record.zoneOffset
                )
            )
        }
        return listOf(weightData)
    }
}
