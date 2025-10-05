package com.oxeai.health.worker

import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.NutritionRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter.Companion.between
import com.oxeai.health.ActivityMetadata
import com.oxeai.health.CaloriesConsumed
import com.oxeai.health.DataConfidence
import com.oxeai.health.DataSource
import com.oxeai.health.Macronutrients
import com.oxeai.health.NutrientAmount
import com.oxeai.health.NutritionData
import com.oxeai.health.NutritionHourly

class NutritionFetcher(context: Context) : HealthDataFetcher(context) {
    suspend fun getNutrition() {
        try {
            val nutritionRequest = ReadRecordsRequest(
                recordType = NutritionRecord::class,
                timeRangeFilter = between(startTime, endTime)
            )
            val nutritionRecords = healthConnectClient.readRecords(nutritionRequest)

            nutritionRecords.records.firstOrNull()?.let { record ->
                val nutritionData = NutritionData(
                    userId = "user_id", // Replace with actual user ID
                    timestamp = record.startTime,
                    source = DataSource.GOOGLE,
                    metadata = ActivityMetadata(
                        devices = listOf(record.metadata.device.toString()),
                        confidence = DataConfidence.HIGH
                    ),
                    nutrition = NutritionHourly(
                        userId = "user_id",
                        timestamp = record.startTime,
                        source = DataSource.GOOGLE,
                        calories = CaloriesConsumed(
                            consumed = record.energy?.inKilocalories ?: 0.0,
                            unit = "kcal"
                        ),
                        macros = Macronutrients(
                            protein = NutrientAmount(record.protein?.inGrams ?: 0.0, "g"),
                            carbs = NutrientAmount(record.totalCarbohydrate?.inGrams ?: 0.0, "g"),
                            fats = NutrientAmount(record.totalFat?.inGrams ?: 0.0, "g"),
                            fiber = NutrientAmount(record.dietaryFiber?.inGrams ?: 0.0, "g")
                        )
                    )
                )
                saveDataAsJson(nutritionData)
                sendDataToApi(nutritionData)
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error reading health data", e)
        }
    }

    companion object {
        private const val TAG = "NutritionFetcher"
    }
}
