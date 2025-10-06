package com.oxeai.healthconnect.fetchers

import android.content.Context
import androidx.health.connect.client.records.LeanBodyMassRecord
import androidx.health.connect.client.response.ReadRecordsResponse
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.LeanBodyMassData
import com.oxeai.healthconnect.models.Metadata
import com.oxeai.healthconnect.models.TrackedMeasurement
import java.util.UUID

class LeanBodyMassFetcher(context: Context, userId: UUID) :
    HealthDataFetcher<LeanBodyMassRecord>(context, userId, LeanBodyMassRecord::class) {

    override fun processRecords(response: ReadRecordsResponse<LeanBodyMassRecord>): LeanBodyMassData {
        val leanBodyMassData = LeanBodyMassData(
            userId = userId,
            timestamp = endTime,
            source = DataSource.GOOGLE,
            metadata = Metadata(
                devices = getDeviceModels(response),
                confidence = DataConfidence.HIGH
            )
        )
        response.records.filter { it -> it.mass.inGrams > 0 }.forEach { record ->
            leanBodyMassData.measurements.add(
                TrackedMeasurement(
                    value = record.mass.inGrams,
                    unit = "g",
                    recordedAt = record.time
                )
            )
        }
        return leanBodyMassData
    }
}

