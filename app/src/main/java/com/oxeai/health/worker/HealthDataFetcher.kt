package com.oxeai.health.worker

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.health.connect.client.HealthConnectClient
import com.oxeai.health.BaseHealthData
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.time.Instant
import java.time.temporal.ChronoUnit

open class HealthDataFetcher(context: Context) {
    protected val healthConnectClient = HealthConnectClient.getOrCreate(context)
    protected val endTime = Instant.now()
    protected val startTime = endTime.minus(1, ChronoUnit.HOURS)


    protected fun saveDataAsJson(data: BaseHealthData) {
        try {
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "health_data.json")
//            val file = File(context.filesDir, "health_data.json")
            val writer = FileWriter(file, true) // Append to the file
            writer.append(Json.encodeToString(data)).append("\n")
            writer.flush()
            writer.close()
            Log.d(TAG, "Health data saved to: " + file.absolutePath)
        } catch (e: IOException) {
            Log.e(TAG, "Error saving health data to file", e)
        }
    }

    protected fun sendDataToApi(data: BaseHealthData) {
        // In a real application, you would use a networking library like Retrofit or Volley
        // to send the data to a secure API endpoint.
        Log.d(TAG, "Sending data to API: " + data)
        // Placeholder for API call
    }

    companion object {
        private const val TAG = "HealthDataFetcher"
    }
}
