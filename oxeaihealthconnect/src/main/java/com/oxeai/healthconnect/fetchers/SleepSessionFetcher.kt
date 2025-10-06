package com.oxeai.healthconnect.fetchers

import android.content.Context
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.SleepSessionRecord.Companion.STAGE_TYPE_AWAKE
import androidx.health.connect.client.records.SleepSessionRecord.Companion.STAGE_TYPE_DEEP
import androidx.health.connect.client.records.SleepSessionRecord.Companion.STAGE_TYPE_LIGHT
import androidx.health.connect.client.records.SleepSessionRecord.Companion.STAGE_TYPE_OUT_OF_BED
import androidx.health.connect.client.records.SleepSessionRecord.Companion.STAGE_TYPE_REM
import androidx.health.connect.client.records.SleepSessionRecord.Companion.STAGE_TYPE_SLEEPING
import androidx.health.connect.client.response.ReadRecordsResponse
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.Metadata
import com.oxeai.healthconnect.models.SleepSession
import com.oxeai.healthconnect.models.SleepSessionData
import com.oxeai.healthconnect.models.SleepStage
import java.util.UUID

class SleepSessionFetcher(context: Context, userId: UUID) : HealthDataFetcher<SleepSessionRecord>(context, userId, SleepSessionRecord::class) {

    override fun processRecords(response: ReadRecordsResponse<SleepSessionRecord>): SleepSessionData {
        val sleepSessionData = SleepSessionData(
            userId = userId,
            timestamp = endTime,
            source = DataSource.GOOGLE,
            metadata = Metadata(
                devices = getDeviceModels(response),
                confidence = DataConfidence.HIGH
            )
        )
        response.records.filter { record -> record.stages.isNotEmpty() }.forEach { record ->
            record.stages.forEach { stage ->
                sleepSessionData.measurements.add(
                    SleepSession(
                        startTime = stage.startTime,
                        stage = stage.stage.toSleepStage(),
                        endTime = stage.endTime,
                    )
                )
            }
        }
        return sleepSessionData
    }

    companion object {
        fun Int.toSleepStage(): SleepStage {
            return when (this) {
                STAGE_TYPE_AWAKE -> SleepStage.AWAKE
                STAGE_TYPE_SLEEPING -> SleepStage.SLEEP
                STAGE_TYPE_OUT_OF_BED -> SleepStage.OUT_OF_BED
                STAGE_TYPE_LIGHT -> SleepStage.LIGHT
                STAGE_TYPE_DEEP -> SleepStage.DEEP
                STAGE_TYPE_REM -> SleepStage.REM
                else -> SleepStage.UNKNOWN
            }
        }
    }
}