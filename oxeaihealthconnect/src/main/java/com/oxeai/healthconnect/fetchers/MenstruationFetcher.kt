package com.oxeai.healthconnect.fetchers

import android.content.Context
import androidx.health.connect.client.records.MenstruationFlowRecord
import androidx.health.connect.client.response.ReadRecordsResponse
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.Flow
import com.oxeai.healthconnect.models.FlowMeasurement
import com.oxeai.healthconnect.models.MenstruationData
import com.oxeai.healthconnect.models.Metadata
import java.util.UUID

class MenstruationFetcher(context: Context, userId: UUID) :
    HealthDataFetcher<MenstruationFlowRecord>(context, userId, MenstruationFlowRecord::class) {

    override fun processRecords(response: ReadRecordsResponse<MenstruationFlowRecord>): List<MenstruationData> {
        val menstruationData = MenstruationData(
            userId = userId,
            timestamp = endTime,
            source = DataSource.GOOGLE,
            metadata = Metadata(
                devices = getDeviceModels(response),
                confidence = DataConfidence.HIGH
            )
        )
        response.records.filter { it -> it.flow > 0 }.forEach { record ->
            menstruationData.measurements.add(
                FlowMeasurement(
                    flow = record.flow.toFlow(),
                    recordedAt = record.time
                )
            )
        }
        return listOf(menstruationData)
    }

    companion object {
        fun Int.toFlow(): Flow {
            return when (this) {
                MenstruationFlowRecord.FLOW_UNKNOWN -> Flow.Unknown
                MenstruationFlowRecord.FLOW_LIGHT -> Flow.Light
                MenstruationFlowRecord.FLOW_MEDIUM -> Flow.Medium
                MenstruationFlowRecord.FLOW_HEAVY -> Flow.Heavy
                else -> Flow.Unknown
            }
        }
    }
}