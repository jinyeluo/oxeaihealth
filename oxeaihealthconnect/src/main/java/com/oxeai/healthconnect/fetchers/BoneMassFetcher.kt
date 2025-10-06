package com.oxeai.healthconnect.fetchers

import android.content.Context
import androidx.health.connect.client.records.BoneMassRecord
import androidx.health.connect.client.response.ReadRecordsResponse
import com.oxeai.healthconnect.models.BoneMassData
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.Metadata
import com.oxeai.healthconnect.models.TrackedMeasurement
import java.util.UUID

class BoneMassFetcher(context: Context, userId: UUID) : HealthDataFetcher<BoneMassRecord>(context, userId, BoneMassRecord::class) {

    override fun processRecords(response: ReadRecordsResponse<BoneMassRecord>): BoneMassData {
        val boneMassData = BoneMassData(
            userId = userId,
            timestamp = endTime,
            source = DataSource.GOOGLE,
            metadata = Metadata(
                devices = getDeviceModels(response),
                confidence = DataConfidence.HIGH
            )
        )
        response.records.filter { record -> record.mass.inGrams > 0 }.forEach { record ->
            boneMassData.measurements.add(
                TrackedMeasurement(
                    value = record.mass.inGrams,
                    unit = "g",
                    recordedAt = record.time
                )
            )
        }
        return boneMassData
    }
}
