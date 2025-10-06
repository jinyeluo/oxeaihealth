package com.oxeai.healthconnect.fetchers

import android.content.Context
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.response.ReadRecordsResponse
import com.oxeai.healthconnect.models.BloodPressure
import com.oxeai.healthconnect.models.BloodPressureData
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.Metadata
import java.util.UUID

class BloodPressureFetcher(context: Context, userId: UUID) :
    HealthDataFetcher<BloodPressureRecord>(context, userId, BloodPressureRecord::class) {

    override fun processRecords(response: ReadRecordsResponse<BloodPressureRecord>): BloodPressureData {
        val bloodPressureData = BloodPressureData(
            userId = userId,
            timestamp = endTime,
            source = DataSource.GOOGLE,
            measurements = ArrayList(),
            metadata = Metadata(
                devices = getDeviceModels(response),
                confidence = DataConfidence.HIGH
            )
        )
        response.records.filter { record -> record.systolic.inMillimetersOfMercury > 0 }.forEach { record ->
            bloodPressureData.measurements.add(
                BloodPressure(
                    record.systolic.inMillimetersOfMercury,
                    record.diastolic.inMillimetersOfMercury,
                    unit = "mmHg",
                    recordedAt = record.time
                ),
            )
        }
        return bloodPressureData
    }
}