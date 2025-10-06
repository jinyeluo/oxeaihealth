package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.ReadRecordsResponse
import androidx.health.connect.client.time.TimeRangeFilter
import com.oxeai.healthconnect.models.BaseHealthData
import com.oxeai.healthconnect.models.MealType.BREAKFAST
import com.oxeai.healthconnect.models.MealType.DINNER
import com.oxeai.healthconnect.models.MealType.LUNCH
import com.oxeai.healthconnect.models.MealType.SNACK
import com.oxeai.healthconnect.models.MealType.UNKNOWN
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.time.Instant
import java.time.temporal.ChronoUnit

import java.util.UUID
import kotlin.reflect.KClass

abstract class HealthDataFetcher<T : Record>(
    protected val context: Context,
    protected val userId: UUID,
    private val recordType: KClass<T>
) {
    protected val healthConnectClient = HealthConnectClient.getOrCreate(context)
    protected val endTime = Instant.now()
    protected val startTime = endTime.minus(1, ChronoUnit.HOURS)

    abstract fun processRecords(response: ReadRecordsResponse<T>): BaseHealthData

    suspend fun fetchData() {
        try {
            val permissions = healthConnectClient.permissionController.getGrantedPermissions()
            if (HealthPermission.getReadPermission(recordType) !in permissions) {
                Log.w(TAG, "Read permission for ${recordType.simpleName} is not granted.")
                return
            }

            val request = ReadRecordsRequest(
                recordType = recordType,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
            val response = healthConnectClient.readRecords(request)

            if (response.records.isNotEmpty()) {
                val data = processRecords(response)
                if (data.isValid()) {
                    saveDataAsJson(data)
                    sendDataToApi(data)
                }
            }
        } catch (e: Exception) {
            onError(e)
        }
    }

    protected fun saveDataAsJson(data: BaseHealthData) {
        try {
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "health_data.json")
//            val file = File(context.filesDir, "health_data.json")
            val writer = FileWriter(file, true) // Append to the file
            writer.append(Json.encodeToString(BaseHealthData.serializer(), data)).append("\n")
            writer.flush()
            writer.close()
            Log.d(TAG, "Health data saved to: " + file.absolutePath)
        } catch (e: IOException) {
            onError(e)
        }
    }

    protected fun sendDataToApi(data: BaseHealthData) {
        // In a real application, you would use a networking library like Retrofit or Volley
        // to send the data to a secure API endpoint.
        Log.d(TAG, "Sending data to API: " + data)
        // Placeholder for API call
    }

    protected fun onError(e: Exception) {
        //todo: handle error
        Log.e(TAG, "Error reading data", e)
    }

    companion object {
        private const val TAG = "HealthDataFetcher"
        fun <T : Record> getDeviceModels(response: ReadRecordsResponse<T>): List<String> {
            return response.records
                .mapNotNull { it.metadata.device?.model }
                .distinct()
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

fun Double.isZero(epsilon: Double = 1e-10): Boolean {
    return kotlin.math.abs(this) < epsilon
}
