package com.oxeai.health.worker

import android.content.Context
import android.util.Log
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.SpeedRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.records.Vo2MaxRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter.Companion.between
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class HealthDataWorker(context: Context, workerParams: WorkerParameters) : ListenableWorker(context, workerParams) {
    private val executor: Executor = Executors.newSingleThreadExecutor()

    override fun startWork(): ListenableFuture<Result?> {
        return CallbackToFutureAdapter.getFuture<Result?>(CallbackToFutureAdapter.Resolver { completer: CallbackToFutureAdapter.Completer<Result?>? ->
            executor.execute {
                try {
                    val healthData = aggregateHealthData()
                    if (healthData != null) {
                        saveDataToFile(healthData)
                        sendDataToApi(healthData)
                        completer!!.set(Result.success())
                    } else {
                        completer!!.set(Result.failure())
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error in doWork", e)
                    completer!!.setException(e)
                }
            }
            "HealthDataWorker"
        })
    }


    @Throws(IOException::class)
    private fun aggregateHealthData(): String? {
        val healthConnectClient = HealthConnectClient.getOrCreate(applicationContext)
        val endTime = Instant.now()
        val startTime = endTime.minus(1, ChronoUnit.HOURS)

        try {
            val stepsRequest: ReadRecordsRequest<StepsRecord> = ReadRecordsRequest(
                StepsRecord::class, between(startTime, endTime)
            )
            val heartRateRequest: ReadRecordsRequest<HeartRateRecord> = ReadRecordsRequest(
                HeartRateRecord::class,
                between(startTime, endTime)
            )
            val exerciseSessionRequest: ReadRecordsRequest<ExerciseSessionRecord> = ReadRecordsRequest(
                ExerciseSessionRecord::class,
                between(startTime, endTime)
            )

            // Since you need a synchronous call, use runBlocking
            val stepsRecords: List<StepsRecord> = runBlocking {
                healthConnectClient.readRecords(stepsRequest).records
            }
            val totalSteps = stepsRecords.sumOf { it.count }

            val heartRateRecords: List<HeartRateRecord> = runBlocking {
                healthConnectClient.readRecords(heartRateRequest).records
            }
            val avgHeartRate = heartRateRecords.flatMap { it.samples }.map { it.beatsPerMinute }.average()


            val exerciseSessions: List<ExerciseSessionRecord> = runBlocking {
                healthConnectClient.readRecords(exerciseSessionRequest).records
            }

            val exerciseSummary = exerciseSessions.joinToString(separator = "\n") {
                "Exercise: ${it.exerciseType}, Duration: ${it.endTime.epochSecond - it.startTime.epochSecond}s"
            }

            val activeCaloriesRequest: ReadRecordsRequest<ActiveCaloriesBurnedRecord> = ReadRecordsRequest(
                ActiveCaloriesBurnedRecord::class,
                between(startTime, endTime)
            )
            val activeCaloriesRecords: List<ActiveCaloriesBurnedRecord> = runBlocking {
                healthConnectClient.readRecords(activeCaloriesRequest).records
            }
            val totalActiveCalories = activeCaloriesRecords.sumOf { it.energy.inCalories }

            val totalCaloriesRequest: ReadRecordsRequest<TotalCaloriesBurnedRecord> = ReadRecordsRequest(
                TotalCaloriesBurnedRecord::class,
                between(startTime, endTime)
            )
            val totalCaloriesRecords: List<TotalCaloriesBurnedRecord> = runBlocking {
                healthConnectClient.readRecords(totalCaloriesRequest).records
            }
            val totalCalories = totalCaloriesRecords.sumOf { it.energy.inCalories }

            val distanceRequest: ReadRecordsRequest<DistanceRecord> = ReadRecordsRequest(
                DistanceRecord::class,
                between(startTime, endTime)
            )
            val distanceRecords: List<DistanceRecord> = runBlocking {
                healthConnectClient.readRecords(distanceRequest).records
            }
            val totalDistance = distanceRecords.sumOf { it.distance.inMeters }

            val speedRequest: ReadRecordsRequest<SpeedRecord> = ReadRecordsRequest(
                SpeedRecord::class,
                between(startTime, endTime)
            )
            val speedRecords: List<SpeedRecord> = runBlocking {
                healthConnectClient.readRecords(speedRequest).records
            }
            val avgSpeed = speedRecords.flatMap { it.samples }.map { it.speed.inMetersPerSecond }.average()

            // Move Minutes and Heart Points not avail in GoogleHealth

            val vo2MaxRequest: ReadRecordsRequest<Vo2MaxRecord> = ReadRecordsRequest(
                Vo2MaxRecord::class,
                between(startTime, endTime)
            )
            val vo2MaxRecords: List<Vo2MaxRecord> = runBlocking {
                healthConnectClient.readRecords(vo2MaxRequest).records
            }
            val avgVo2Max = vo2MaxRecords.map { it.vo2MillilitersPerMinuteKilogram }.average()

            return "Heart Rate: $avgHeartRate bpm, Steps: $totalSteps, " +
                    "Active Calories: $totalActiveCalories, " +
                    "Total Calories: $totalCalories," +
                    " Distance: $totalDistance m, " +
                    "Speed: $avgSpeed m/s, " +
                    " VO2 Max: $avgVo2Max, Timestamp: $endTime\n$exerciseSummary"

        } catch (e: Exception) {
            Log.e(TAG, "Error reading health data", e)
            return null
        }
    }

    private fun saveDataToFile(data: String?) {
        try {
            val file = File(getApplicationContext().getFilesDir(), "health_data.txt")
            val writer = FileWriter(file, true) // Append to the file
            writer.append(data).append("\n")
            writer.flush()
            writer.close()
            Log.d(TAG, "Health data saved to: " + file.getAbsolutePath())
        } catch (e: IOException) {
            Log.e(TAG, "Error saving health data to file", e)
        }
    }

    private fun sendDataToApi(data: String?) {
        // In a real application, you would use a networking library like Retrofit or Volley
        // to send the data to a secure API endpoint.
        Log.d(TAG, "Sending data to API: " + data)
        // Placeholder for API call
    }

    companion object {
        private const val TAG = "HealthDataWorker"
    }
}
