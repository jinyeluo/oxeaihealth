package com.oxeai.healthconnect.fetchers

import android.content.Context
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.response.ReadRecordsResponse
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.HeartRateData
import com.oxeai.healthconnect.models.HeartRateSample
import com.oxeai.healthconnect.models.Metadata
import java.util.UUID

class HeartRateFetcher(context: Context, userId: UUID) : HealthDataFetcher<HeartRateRecord>(context, userId, HeartRateRecord::class) {

    override fun processRecords(response: ReadRecordsResponse<HeartRateRecord>): List<HeartRateData> {
        val heartRateData = HeartRateData(
            userId = userId,
            timestamp = endTime,
            source = DataSource.GOOGLE,
            metadata = Metadata(
                devices = getDeviceModels(response),
                confidence = DataConfidence.HIGH
            )
        )
        response.records.filter { record -> record.samples.isNotEmpty() }.forEach { record ->
            record.samples.filter { sample -> sample.beatsPerMinute > 0 }.forEach { sample ->
                heartRateData.measurements.add(
                    HeartRateSample(
                        beatsPerMinute = sample.beatsPerMinute.toInt(),
                        time = sample.time
                    )
                )
            }
        }
        return listOf(heartRateData)
    }
}
