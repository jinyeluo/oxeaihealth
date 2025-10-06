package com.oxeai.healthconnect.fetchers

import android.content.Context
import androidx.health.connect.client.records.BodyTemperatureRecord
import androidx.health.connect.client.response.ReadRecordsResponse
import com.oxeai.healthconnect.models.BodyTemperatureData
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.Metadata
import com.oxeai.healthconnect.models.TemperatureReading
import java.util.UUID

class BodyTemperatureFetcher(context: Context, userId: UUID) : HealthDataFetcher<BodyTemperatureRecord>(context, userId, BodyTemperatureRecord::class) {

    override fun processRecords(response: ReadRecordsResponse<BodyTemperatureRecord>): BodyTemperatureData {
        val bodyTemperatureData = BodyTemperatureData(
            userId = userId,
            timestamp = endTime,
            source = DataSource.GOOGLE,
            measurements = ArrayList(),
            metadata = Metadata(
                devices = getDeviceModels(response),
                confidence = DataConfidence.HIGH
            )
        )
        response.records.filter { record -> record.temperature.inCelsius > 0 }.forEach { record ->
            bodyTemperatureData.measurements.add(
                TemperatureReading(
                    value = record.temperature.inCelsius,
                    unit = "celsius",
                    recordedAt = record.time
                )
            )
        }
        return bodyTemperatureData
    }
}
