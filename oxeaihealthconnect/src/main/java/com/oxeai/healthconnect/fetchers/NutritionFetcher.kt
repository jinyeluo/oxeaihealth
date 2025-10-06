package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.NutritionRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter.Companion.between
import com.oxeai.healthconnect.models.ActivityMetadata
import com.oxeai.healthconnect.models.CaloriesConsumed
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.Macronutrients
import com.oxeai.healthconnect.models.NutrientAmount
import com.oxeai.healthconnect.models.Nutrition
import com.oxeai.healthconnect.models.NutritionData
import java.util.UUID

class NutritionFetcher(context: Context, userId: UUID) : HealthDataFetcher(context, userId) {
    suspend fun getNutrition() {
        try {
            val permissions = healthConnectClient.permissionController.getGrantedPermissions()
            if (HealthPermission.getReadPermission(NutritionRecord::class) !in permissions) {
                Log.w(TAG, "Read permission for NutritionRecord is not granted.")
                return
            }

            val nutritionRequest = ReadRecordsRequest(
                recordType = NutritionRecord::class,
                timeRangeFilter = between(startTime, endTime)
            )
            val nutritionRecords = healthConnectClient.readRecords(nutritionRequest)

            nutritionRecords.records.firstOrNull()?.let { record ->
                val nutritionData = NutritionData(
                    userId = userId,
                    timestamp = record.startTime,
                    source = DataSource.GOOGLE,
                    metadata = ActivityMetadata(
                        devices = getDeviceModels(nutritionRecords),
                        confidence = DataConfidence.HIGH
                    ),
                    nutrition = Nutrition(
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
            onError(e)
        }
    }

    companion object {
        private const val TAG = "NutritionFetcher"
    }
}
