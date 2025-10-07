package com.oxeai.healthconnect.fetchers

import android.content.Context
import androidx.health.connect.client.records.PowerRecord
import androidx.health.connect.client.response.ReadRecordsResponse
import com.oxeai.healthconnect.models.BaseHealthData
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.Metadata
import com.oxeai.healthconnect.models.PowerData
import com.oxeai.healthconnect.models.TrackedMeasurement
import java.util.UUID

class PowerFetcher(context: Context, userId: UUID) : HealthDataFetcher<PowerRecord>(context, userId, PowerRecord::class) {

    override fun processRecords(response: ReadRecordsResponse<PowerRecord>): List<BaseHealthData> {
        val result = ArrayList<BaseHealthData>()

        response.records.filter { record -> record.samples.isNotEmpty() }.forEach { record ->
            val powerData = PowerData(
                userId = userId,
                timestamp = endTime,
                source = DataSource.GOOGLE,
                metadata = Metadata(
                    devices = getDeviceModels(response),
                    confidence = DataConfidence.HIGH
                ),
                startTime = record.startTime,
                startZoneOffset = record.startZoneOffset,
                endTime = record.endTime,
                endZoneOffset = record.endZoneOffset
            )
            record.samples.forEach { sample ->
                powerData.measurements.add(
                    TrackedMeasurement(
                        value = sample.power.inWatts,
                        unit = "w",
                        recordedAt = sample.time,
                        timeZoneOffset = null
                    )
                )
            }
            result.add(powerData)
        }
        return result
    }
}