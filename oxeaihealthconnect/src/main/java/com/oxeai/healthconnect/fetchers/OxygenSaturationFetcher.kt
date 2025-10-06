package com.oxeai.healthconnect.fetchers

import android.content.Context
import androidx.health.connect.client.records.OxygenSaturationRecord
import androidx.health.connect.client.response.ReadRecordsResponse
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.Metadata
import com.oxeai.healthconnect.models.OxygenSaturationData
import com.oxeai.healthconnect.models.PercentageMeasurement
import java.util.UUID

class OxygenSaturationFetcher(context: Context, userId: UUID) : HealthDataFetcher<OxygenSaturationRecord>(context, userId, OxygenSaturationRecord::class) {

    override fun processRecords(response: ReadRecordsResponse<OxygenSaturationRecord>): OxygenSaturationData {
        val oxygenSaturationData = OxygenSaturationData(
            userId = userId,
            timestamp = endTime,
            source = DataSource.GOOGLE,
            metadata = Metadata(
                devices = getDeviceModels(response),
                confidence = DataConfidence.HIGH
            )
        )
        response.records.filter { record -> record.percentage.value > 0 }.forEach { record ->
            oxygenSaturationData.measurements.add(
                PercentageMeasurement(
                    percentage = record.percentage.value,
                    recordedAt = record.time
                )
            )
        }
        return oxygenSaturationData
    }
}