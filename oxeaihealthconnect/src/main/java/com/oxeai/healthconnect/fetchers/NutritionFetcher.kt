package com.oxeai.healthconnect.fetchers

import android.content.Context
import androidx.health.connect.client.records.NutritionRecord
import androidx.health.connect.client.response.ReadRecordsResponse
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.Metadata
import com.oxeai.healthconnect.models.Nutrition
import com.oxeai.healthconnect.models.NutritionData
import com.oxeai.healthconnect.models.SimpleMeasurement
import java.util.UUID

class NutritionFetcher(context: Context, userId: UUID) :
    HealthDataFetcher<NutritionRecord>(context, userId, NutritionRecord::class) {

    override fun processRecords(response: ReadRecordsResponse<NutritionRecord>): List<NutritionData> {
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
                    mealType = record.mealType.toMealType(),
                    biotin = record.biotin?.inGrams?.let { SimpleMeasurement(it, "g") },
                    caffeine = record.caffeine?.inGrams?.let { SimpleMeasurement(it, "g") },
                    calcium = record.calcium?.inGrams?.let { SimpleMeasurement(it, "g") },
                    energy = record.energy?.inKilocalories?.let { SimpleMeasurement(it, "kcal") },
                    energyFromFat = record.energyFromFat?.inKilocalories?.let { SimpleMeasurement(it, "kcal") },
                    chloride = record.chloride?.inGrams?.let { SimpleMeasurement(it, "g") },
                    cholesterol = record.cholesterol?.inGrams?.let { SimpleMeasurement(it, "g") },
                    chromium = record.chromium?.inGrams?.let { SimpleMeasurement(it, "g") },
                    copper = record.copper?.inGrams?.let { SimpleMeasurement(it, "g") },
                    dietaryFiber = record.dietaryFiber?.inGrams?.let { SimpleMeasurement(it, "g") },
                    folate = record.folate?.inGrams?.let { SimpleMeasurement(it, "g") },
                    folicAcid = record.folicAcid?.inGrams?.let { SimpleMeasurement(it, "g") },
                    iodine = record.iodine?.inGrams?.let { SimpleMeasurement(it, "g") },
                    iron = record.iron?.inGrams?.let { SimpleMeasurement(it, "g") },
                    magnesium = record.magnesium?.inGrams?.let { SimpleMeasurement(it, "g") },
                    manganese = record.manganese?.inGrams?.let { SimpleMeasurement(it, "g") },
                    molybdenum = record.molybdenum?.inGrams?.let { SimpleMeasurement(it, "g") },
                    monounsaturatedFat = record.monounsaturatedFat?.inGrams?.let { SimpleMeasurement(it, "g") },
                    niacin = record.niacin?.inGrams?.let { SimpleMeasurement(it, "g") },
                    pantothenicAcid = record.pantothenicAcid?.inGrams?.let { SimpleMeasurement(it, "g") },
                    phosphorus = record.phosphorus?.inGrams?.let { SimpleMeasurement(it, "g") },
                    polyunsaturatedFat = record.polyunsaturatedFat?.inGrams?.let { SimpleMeasurement(it, "g") },
                    potassium = record.potassium?.inGrams?.let { SimpleMeasurement(it, "g") },
                    protein = record.protein?.inGrams?.let { SimpleMeasurement(it, "g") },
                    riboflavin = record.riboflavin?.inGrams?.let { SimpleMeasurement(it, "g") },
                    saturatedFat = record.saturatedFat?.inGrams?.let { SimpleMeasurement(it, "g") },
                    selenium = record.selenium?.inGrams?.let { SimpleMeasurement(it, "g") },
                    sodium = record.sodium?.inGrams?.let { SimpleMeasurement(it, "g") },
                    sugar = record.sugar?.inGrams?.let { SimpleMeasurement(it, "g") },
                    thiamin = record.thiamin?.inGrams?.let { SimpleMeasurement(it, "g") },
                    totalCarbohydrate = record.totalCarbohydrate?.inGrams?.let { SimpleMeasurement(it, "g") },
                    totalFat = record.totalFat?.inGrams?.let { SimpleMeasurement(it, "g") },
                    transFat = record.transFat?.inGrams?.let { SimpleMeasurement(it, "g") },
                    unsaturatedFat = record.unsaturatedFat?.inGrams?.let { SimpleMeasurement(it, "g") },
                    vitaminA = record.vitaminA?.inGrams?.let { SimpleMeasurement(it, "g") },
                    vitaminB12 = record.vitaminB12?.inGrams?.let { SimpleMeasurement(it, "g") },
                    vitaminB6 = record.vitaminB6?.inGrams?.let { SimpleMeasurement(it, "g") },
                    vitaminC = record.vitaminC?.inGrams?.let { SimpleMeasurement(it, "g") },
                    vitaminD = record.vitaminD?.inGrams?.let { SimpleMeasurement(it, "g") },
                    vitaminE = record.vitaminE?.inGrams?.let { SimpleMeasurement(it, "g") },
                    vitaminK = record.vitaminK?.inGrams?.let { SimpleMeasurement(it, "g") },
                    zinc = record.zinc?.inGrams?.let { SimpleMeasurement(it, "g") }
                )
            )
        }
        return listOf(nutritionData)
    }
}