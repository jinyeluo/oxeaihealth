package com.oxeai.healthconnect.fetchers

import android.content.Context
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.response.ReadRecordsResponse
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.DistanceData
import com.oxeai.healthconnect.models.IntervalMeasurement
import com.oxeai.healthconnect.models.Metadata
import java.util.UUID

class DistanceFetcher(context: Context, userId: UUID) : HealthDataFetcher<DistanceRecord>(context, userId, DistanceRecord::class) {

    override fun processRecords(response: ReadRecordsResponse<DistanceRecord>): DistanceData {
        val distanceData = DistanceData(
            userId = userId,
            timestamp = endTime,
            source = DataSource.GOOGLE,
            metadata = Metadata(
                devices = getDeviceModels(response),
                confidence = DataConfidence.HIGH
            )
        )
        response.records.filter { record -> record.distance.inMeters > 0 }.forEach { record ->
            distanceData.measurements.add(
                IntervalMeasurement(
                    value = record.distance.inMeters,
                    unit = "m",
                    startTime = record.startTime,
                    endTime = record.endTime
                )
            )
        }
        return distanceData
    }
}
