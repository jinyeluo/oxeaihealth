package com.oxeai.healthconnect.fetchers

import android.content.Context
import androidx.health.connect.client.records.FloorsClimbedRecord
import androidx.health.connect.client.response.ReadRecordsResponse
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.FloorsClimbedData
import com.oxeai.healthconnect.models.IntervalMeasurement
import com.oxeai.healthconnect.models.Metadata
import java.util.UUID

class FloorsClimbedFetcher(context: Context, userId: UUID) : HealthDataFetcher<FloorsClimbedRecord>(context, userId, FloorsClimbedRecord::class) {

    override fun processRecords(response: ReadRecordsResponse<FloorsClimbedRecord>): List<FloorsClimbedData> {
        val floorsClimbedData = FloorsClimbedData(
            userId = userId,
            timestamp = endTime,
            source = DataSource.GOOGLE,
            metadata = Metadata(
                devices = getDeviceModels(response),
                confidence = DataConfidence.HIGH
            )
        )
        response.records.filter { record -> record.floors > 0 }.forEach { record ->
            floorsClimbedData.measurements.add(
                IntervalMeasurement(
                    value = record.floors,
                    unit = "floor",
                    startTime = record.startTime,
                    endTime = record.endTime, startZoneOffset = record.startZoneOffset,
                    endZoneOffset = record.endZoneOffset
                )
            )
        }
        return listOf(floorsClimbedData)
    }
}
