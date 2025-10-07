package com.oxeai.healthconnect.fetchers

import android.content.Context
import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.response.ReadRecordsResponse
import com.oxeai.healthconnect.models.BloodGlucoseData
import com.oxeai.healthconnect.models.BloodGlucoseReading
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.GlucoseContext
import com.oxeai.healthconnect.models.Metadata
import com.oxeai.healthconnect.models.RelationToMeal
import java.util.UUID

class BloodGlucoseFetcher(context: Context, userId: UUID) : HealthDataFetcher<BloodGlucoseRecord>(context, userId, BloodGlucoseRecord::class) {
    override fun processRecords(response: ReadRecordsResponse<BloodGlucoseRecord>): List<BloodGlucoseData> {
        val bloodGlucoseData = BloodGlucoseData(
            userId = userId,
            timestamp = endTime,
            source = DataSource.GOOGLE,
            metadata = Metadata(
                devices = getDeviceModels(response),
                confidence = DataConfidence.HIGH
            )

        )

        response.records.filter { record -> record.level.inMilligramsPerDeciliter > 0 }.forEach { record ->
            bloodGlucoseData.measurements.add(
                BloodGlucoseReading(
                    value = record.level.inMilligramsPerDeciliter,
                    unit = "mg/dL",
                    context = GlucoseContext(record.mealType.toMealType(), record.relationToMeal.toRelationToMeal()),
                    recordedAt = record.time
                )
            )
        }
        return listOf(bloodGlucoseData)
    }

    companion object {
        fun Int.toRelationToMeal(): RelationToMeal {
            return when (this) {
                BloodGlucoseRecord.RELATION_TO_MEAL_GENERAL -> RelationToMeal.GENERAL
                BloodGlucoseRecord.RELATION_TO_MEAL_FASTING -> RelationToMeal.FASTING
                BloodGlucoseRecord.RELATION_TO_MEAL_BEFORE_MEAL -> RelationToMeal.BEFORE_MEAL
                BloodGlucoseRecord.RELATION_TO_MEAL_AFTER_MEAL -> RelationToMeal.AFTER_MEAL
                else -> RelationToMeal.UNKNOWN
            }
        }
    }
}
