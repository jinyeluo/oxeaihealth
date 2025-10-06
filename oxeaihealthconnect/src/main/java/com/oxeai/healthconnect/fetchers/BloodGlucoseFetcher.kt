package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter.Companion.between
import com.oxeai.healthconnect.models.ActivityMetadata
import com.oxeai.healthconnect.models.BloodGlucoseData
import com.oxeai.healthconnect.models.BloodGlucoseReading
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.GlucoseContext
import com.oxeai.healthconnect.models.MealType.BREAKFAST
import com.oxeai.healthconnect.models.MealType.DINNER
import com.oxeai.healthconnect.models.MealType.LUNCH
import com.oxeai.healthconnect.models.MealType.SNACK
import com.oxeai.healthconnect.models.MealType.UNKNOWN
import com.oxeai.healthconnect.models.RelationToMeal
import java.util.UUID

class BloodGlucoseFetcher(context: Context, userId: UUID) : HealthDataFetcher(context, userId) {
    suspend fun getBloodGlucose() {
        try {
            val permissions = healthConnectClient.permissionController.getGrantedPermissions()
            if (HealthPermission.getReadPermission(BloodGlucoseRecord::class) !in permissions) {
                Log.w(TAG, "Read permission for BloodGlucoseRecord is not granted.")
                return
            }

            val bloodGlucoseRequest = ReadRecordsRequest(
                recordType = BloodGlucoseRecord::class,
                timeRangeFilter = between(startTime, endTime)
            )
            val readRecordsResponse = healthConnectClient.readRecords(bloodGlucoseRequest)

            if (readRecordsResponse.records.isEmpty()) {
                Log.i(TAG, "No records found.")
                return
            }

            // just use the first record. I cannot think of a reason to get them all
            readRecordsResponse.records.firstOrNull()?.let { record ->
                val bloodGlucoseData = BloodGlucoseData(
                    userId = userId,
                    timestamp = record.time,
                    source = DataSource.GOOGLE,
                    metadata = ActivityMetadata(
                        devices = getDeviceModels(readRecordsResponse),
                        confidence = DataConfidence.HIGH
                    ),
                    bloodGlucose = BloodGlucoseReading(
                        value = record.level.inMilligramsPerDeciliter,
                        unit = "mg/dL",
                        context = GlucoseContext(record.mealType.toMealType(), record.relationToMeal.toRelationToMeal()),
                        recordedAt = record.time
                    )
                )

                if (record.level.inMilligramsPerDeciliter.isZero()) {
                    Log.w(TAG, "Blood glucose reading is zero. Ignoring.")
                } else {
                    saveDataAsJson(bloodGlucoseData)
                    sendDataToApi(bloodGlucoseData)
                }
            }

        } catch (e: Exception) {
            onError(e)
        }
    }

    companion object {
        private const val TAG = "BloodGlucoseFetcher"

        fun Int.toRelationToMeal(): RelationToMeal {
            return when (this) {
                BloodGlucoseRecord.RELATION_TO_MEAL_GENERAL -> RelationToMeal.GENERAL
                BloodGlucoseRecord.RELATION_TO_MEAL_FASTING -> RelationToMeal.FASTING
                BloodGlucoseRecord.RELATION_TO_MEAL_BEFORE_MEAL -> RelationToMeal.BEFORE_MEAL
                BloodGlucoseRecord.RELATION_TO_MEAL_AFTER_MEAL -> RelationToMeal.AFTER_MEAL
                else -> RelationToMeal.UNKNOWN
            }
        }

        fun Int.toMealType(): com.oxeai.healthconnect.models.MealType {
            return when (this) {
                androidx.health.connect.client.records.MealType.MEAL_TYPE_BREAKFAST -> BREAKFAST
                androidx.health.connect.client.records.MealType.MEAL_TYPE_LUNCH -> LUNCH
                androidx.health.connect.client.records.MealType.MEAL_TYPE_DINNER -> DINNER
                androidx.health.connect.client.records.MealType.MEAL_TYPE_SNACK -> SNACK
                androidx.health.connect.client.records.MealType.MEAL_TYPE_UNKNOWN -> UNKNOWN
                else -> UNKNOWN
            }
        }
    }
}
