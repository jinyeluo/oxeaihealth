package com.oxeai.healthconnect.fetchers

import android.content.Context
import androidx.health.connect.client.records.NutritionRecord
import androidx.health.connect.client.response.ReadRecordsResponse
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.Metadata
import com.oxeai.healthconnect.models.Nutrition
import com.oxeai.healthconnect.models.NutritionData
import java.util.UUID

class NutritionFetcher(context: Context, userId: UUID) :
    HealthDataFetcher<NutritionRecord>(context, userId, NutritionRecord::class) {

    override fun processRecords(response: ReadRecordsResponse<NutritionRecord>): NutritionData {
        val nutritionData = NutritionData(
            userId = userId,
            timestamp = endTime,
            source = DataSource.GOOGLE,
            metadata = Metadata(
                devices = getDeviceModels(response),
                confidence = DataConfidence.HIGH
            )
        )
        response.records.forEach { record ->
            nutritionData.measurements.add(
                Nutrition(
                    startTime = record.startTime,
                    endTime = record.endTime,
                    name = record.name,
                    mealType = record.mealType.toMealType()
//                    todo: complete this
                )
            )
        }
        return nutritionData
    }
}