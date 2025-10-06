package com.oxeai.healthconnect.fetchers

import android.content.Context
import androidx.health.connect.client.records.HeightRecord
import androidx.health.connect.client.response.ReadRecordsResponse
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.HeightData
import com.oxeai.healthconnect.models.Metadata
import com.oxeai.healthconnect.models.TrackedMeasurement
import java.util.UUID

class HeightFetcher(context: Context, userId: UUID) : HealthDataFetcher<HeightRecord>(context, userId, HeightRecord::class) {

    override fun processRecords(response: ReadRecordsResponse<HeightRecord>): HeightData {
        val heightData = HeightData(
            userId = userId,
            timestamp = endTime,
            source = DataSource.GOOGLE,
            metadata = Metadata(
                devices = getDeviceModels(response),
                confidence = DataConfidence.HIGH
            )
        )
        response.records.filter { record -> record.height.inMeters > 0 }.forEach { record ->
            heightData.measurements.add(
                TrackedMeasurement(
                    value = record.height.inMeters,
                    unit = "m",
                    recordedAt = record.time
                )
            )
        }
        return heightData
    }
}

