package com.oxeai.health.worker

import android.content.Context
import android.util.Log
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter.Companion.between
import com.oxeai.health.ActivityMetadata
import com.oxeai.health.DataConfidence
import com.oxeai.health.DataSource
import com.oxeai.health.MoveMinutesData
import com.oxeai.health.TrackedMetric
import kotlinx.coroutines.runBlocking

class MoveMinutesFetcher(context: Context) : HealthDataFetcher(context) {

    suspend fun getMoveMinutes() {
        try {
            val exerciseSessionRequest: ReadRecordsRequest<ExerciseSessionRecord> = ReadRecordsRequest(
                ExerciseSessionRecord::class,
                between(startTime, endTime)
            )
            val exerciseSessions: List<ExerciseSessionRecord> = runBlocking {
                healthConnectClient.readRecords(exerciseSessionRequest).records
            }
            // todo: save exercise sessions instead of just total move minutes
            var totalMoveMinutes = MoveMinutesCalculator().calculateTotalMoveMinutes(exerciseSessions)

            val moveMinutesData = MoveMinutesData(
                userId = "user_id", // Replace with actual user ID
                timestamp = endTime,
                source = DataSource.GOOGLE,
                moveMinutes = TrackedMetric(
                    count = totalMoveMinutes.toInt(),
                    sources = listOf("GoogleFit")
                ),
                metadata = ActivityMetadata(
                    devices = listOf("Unknown"),
                    confidence = DataConfidence.HIGH
                )
            )

            saveDataAsJson(moveMinutesData)
            sendDataToApi(moveMinutesData)
        } catch (e: Exception) {
            Log.e(TAG, "Error reading move minutes data", e)
        }
    }

    companion object {
        private const val TAG = "MoveMinutesFetcher"
    }
}
