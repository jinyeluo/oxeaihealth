package com.oxeai.healthconnect.fetchers

import android.content.Context
import androidx.health.connect.client.records.Vo2MaxRecord
import androidx.health.connect.client.response.ReadRecordsResponse
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.Metadata
import com.oxeai.healthconnect.models.TrackedMeasurement
import com.oxeai.healthconnect.models.Vo2MaxData
import java.util.UUID

class Vo2MaxFetcher(context: Context, userId: UUID) : HealthDataFetcher<Vo2MaxRecord>(context, userId, Vo2MaxRecord::class) {

    override fun processRecords(response: ReadRecordsResponse<Vo2MaxRecord>): List<Vo2MaxData> {
        val vo2MaxData = Vo2MaxData(
            userId = userId,
            timestamp = endTime,
            source = DataSource.GOOGLE,
            metadata = Metadata(
                devices = getDeviceModels(response),
                confidence = DataConfidence.HIGH
            )
        )
        response.records.filter { record -> record.vo2MillilitersPerMinuteKilogram > 0 }.forEach { record ->
            vo2MaxData.measurements.add(
                TrackedMeasurement(
                    value = record.vo2MillilitersPerMinuteKilogram,
                    unit = "ml/min/kg",
                    recordedAt = record.time,
                    timeZoneOffset = record.zoneOffset
                )
            )
        }
        return listOf(vo2MaxData)
    }
}
