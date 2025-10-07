package com.oxeai.healthconnect.fetchers

import android.content.Context
import androidx.health.connect.client.records.SpeedRecord
import androidx.health.connect.client.response.ReadRecordsResponse
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.Metadata
import com.oxeai.healthconnect.models.SpeedData
import com.oxeai.healthconnect.models.TrackedMeasurement
import java.util.UUID

class SpeedFetcher(context: Context, userId: UUID) : HealthDataFetcher<SpeedRecord>(context, userId, SpeedRecord::class) {

    override fun processRecords(response: ReadRecordsResponse<SpeedRecord>): List<SpeedData> {

        var result = ArrayList<SpeedData>()
        response.records.filter { record -> record.samples.isNotEmpty() }.forEach { record ->

            val speedData = SpeedData(
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
                speedData.measurements.add(
                    TrackedMeasurement(
                        recordedAt = sample.time,
                        value = sample.speed.inMetersPerSecond,
                        unit = "m/s",
                        timeZoneOffset = null
                    )
                )
            }
            result.add(speedData)
        }
        return result
    }
}